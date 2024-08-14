package com.example.dexport.exporter;

import com.example.dexport.aop.ExceptionHandler;
import com.example.dexport.aop.DexportFileProcessingException;
import com.example.dexport.config.DexportWriterConfig;
import com.example.dexport.listener.DexportListenerDefault;
import com.example.dexport.listener.DexportListener;
import com.example.dexport.mapper.DexportItemMapper;
import com.example.dexport.storage.DexportFileStorage;
import com.example.dexport.storage.DexportFileStorageDefault;
import com.example.dexport.writer.DexportItemWriter;
import com.example.dexport.writer.DexportItemWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Amirmohammad Khanalipour
 */
public class Dexport implements Dexportable {
    private static final Logger log = LoggerFactory.getLogger(Dexport.class);
    private final DexportFileStorage dexportFileStorage;
    private final DexportListener dexportListener;
    private final ExceptionHandler exceptionHandler;
    private final int threadPoolSize;

    private Dexport(Builder builder) {
        this.dexportFileStorage = builder.dexportFileStorage;
        this.dexportListener = builder.dexportListener;
        this.threadPoolSize = builder.threadPoolSize;
        this.exceptionHandler = new ExceptionHandler(); // Default
    }

    @Override
    public <T> byte[] create(Stream<T> data, DexportItemMapper<T> dexportItemMapper, String format) {
        try {
            return processDataSynchronously(data, getDefaultWriterFromFormat(dexportItemMapper, format));
        } catch (Exception e) {
            exceptionHandler.handleException(e);
            return new byte[0];
        }
    }

    @Override
    public <T> byte[] create(Stream<T> data, DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig) {
        try {
            return processDataSynchronously(data, getWriterWithConfig(dexportItemMapper, dexportWriterConfig));
        } catch (Exception e) {
            exceptionHandler.handleException(e);
            return new byte[0];
        }
    }

    @Override
    public <T> void createAsync(Stream<T> data, DexportItemMapper<T> dexportItemMapper, String format) {
        try {
            processDataAsynchronously(data, getDefaultWriterFromFormat(dexportItemMapper, format));
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @Override
    public <T> void createAsync(Stream<T> data, DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig) {
        try {
            processDataAsynchronously(data, getWriterWithConfig(dexportItemMapper, dexportWriterConfig));
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    private <T> byte[] processDataSynchronously(Stream<T> data, DexportItemWriter<T> dexportItemWriter) {
        // Synchronous processing without ExecutorService for small data sets
        return dexportItemWriter.writeToByteArray(data);
    }

    private <T> void processDataAsynchronously(Stream<T> data, DexportItemWriter<T> dexportItemWriter) {
        ExecutorService executorService = createExecutorService();
        try {
            Future<?> future = processStreamWithExecutor(data, dexportItemWriter, executorService);
            future.get();
        } catch (Exception e) {
            throw new DexportFileProcessingException("Error processing data stream asynchronously", e);
        } finally {
            shutdownExecutorService(executorService);
        }
    }

    private <T> Future<?> processStreamWithExecutor(Stream<T> data, DexportItemWriter<T> dexportItemWriter, ExecutorService executorService) {
        return executorService.submit(() -> {
            String requestId = UUID.randomUUID().toString();
            Path filePath = null;
            try {
                filePath = dexportFileStorage.getFilePath(requestId, dexportItemWriter.getFormat());
                try (OutputStream outputStream = dexportFileStorage.getOutputStream(filePath)) {
                    dexportItemWriter.writeToOutputStream(data, outputStream);
                }

                dexportListener.onFinish(filePath.toAbsolutePath().toString());
            } catch (IOException e) {
                dexportListener.onFailure(filePath != null ? filePath.toAbsolutePath().toString() : null, e);
                throw new DexportFileProcessingException("Error processing data stream with executor", e);
            }
        });
    }

    private void shutdownExecutorService(ExecutorService executorService) {
        log.info("Shutting down ExecutorService...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.warn("ExecutorService did not terminate in the specified time.");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                log.warn("ExecutorService was abruptly shut down. {} tasks will not be executed.", droppedTasks.size());
            }
        } catch (InterruptedException e) {
            log.error("ExecutorService shutdown interrupted", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private <T> DexportItemWriter<T> getDefaultWriterFromFormat(DexportItemMapper<T> dexportItemMapper, String format) {
        return DexportItemWriterFactory.getWriter(dexportItemMapper, format);
    }

    private <T> DexportItemWriter<T> getWriterWithConfig(DexportItemMapper<T> dexportItemMapper, DexportWriterConfig dexportWriterConfig) {
        return DexportItemWriterFactory.getWriter(dexportItemMapper, dexportWriterConfig);
    }

    private ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }

    public static class Builder {
        private DexportFileStorage dexportFileStorage = new DexportFileStorageDefault(); // Default
        private DexportListener dexportListener = new DexportListenerDefault(); // Default
        private int threadPoolSize = Runtime.getRuntime().availableProcessors(); // Default

        public Builder withStorage(DexportFileStorage dexportFileStorage) {
            this.dexportFileStorage = dexportFileStorage;
            return this;
        }

        public Builder withListener(DexportListenerDefault asyncDataExportListener) {
            this.dexportListener = asyncDataExportListener;
            return this;
        }

        public Builder threadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

        public Dexport build() {
            return new Dexport(this);
        }
    }
}
