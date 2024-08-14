# Dexport


## Overview
The **Dexport** is a Java library designed to facilitate the export of data into multiple formats, including CSV, Excel, and PDF. It provides a flexible and efficient way to map and export data, making it ideal for applications that need to generate reports or share data in different formats.


## Features
- **Support for Multiple Formats:** Export data to CSV, Excel, and PDF formats.
- **Synchronous and Asynchronous Processing:** Handle both small and large datasets with ease.
- **Customizable Export Configurations:** Configure export options for PDF files.
- **Easy Integration:** Simple API for integrating with existing applications.


## Requirements
- Java 17 or later
- Apache Commons CSV
- Apache POI
- iText PDF library


## Setup
Add the following dependencies to your Maven `pom.xml` file:

```xml
<dependencies>
    <!-- Apache Commons CSV -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-csv</artifactId>
        <version>1.9.0</version>
    </dependency>

    <!-- Apache POI -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.3</version>
    </dependency>

    <!-- iText PDF -->
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>html2pdf</artifactId>
        <version>4.1.4</version>
    </dependency>
</dependencies>
```


## Usage

### Define Your Data Model
Create a class representing the data you want to export. For example:

```java
public class Foo {
    private String fooName;

    // Getters and setters
}
```


### Implement `DexportItemMapper<T>`
Create an implementation of the ItemMapper interface to map your data fields and headers.

```java
DexportItemMapper<Foo> itemMapper = new DexportItemMapper<>() {
    @Override
    public List<String> mapHeaders() {
        return List.of("Foo Name");
    }

    @Override
    public List<String> mapValues(Foo foo) {
        return List.of(foo.getFooName());
    }
};
```


### Export Data Synchronously
For smaller datasets, you can export data synchronously:

```java
List<Foo> fooList = ...; // Your data source

Dexport dexport = new Dexport.Builder().build();
byte[] csvBytes = dexport.create(fooList.stream(), itemMapper, "csv");
byte[] excelBytes = dexport.create(fooList.stream(), itemMapper, "excel");
byte[] pdfBytes = dexport.create(fooList.stream(), itemMapper, "pdf");
```


### Export Data Asynchronously
For large datasets, use asynchronous processing:

```java
Dexport dexport = new Dexport.Builder().build();
dexport.createAsync(fooList.stream(), itemMapper, "pdf");
```


### Configuration for Dexport
To create a Dexport instance with default config:

```java
Dexport dexport = new Dexport.Builder().build();
```

To customize configuration:

```java
Dexport dexport = new Dexport
                .Builder()
                .withStorage(fileStorage) // Configure a custom storage config
                .withListener(listener) // Configure a custom listener for async process
                .threadPoolSize(poolSize) // Configure thread pool size
```


## Key Classes
- **Dexport: The main class for exporting data. Supports both synchronous and asynchronous methods.
- **DexportItemMapper<T>: Interface for mapping data fields and headers.
- **DexportWriterConfig: Configuration class for customizing export options.
- **DexportCsvItemWriter, DexportExcelItemWriter, DexportPdfItemWriter: Classes for handling specific file formats.

## Contributing
Feel free to fork the repository and submit pull requests for improvements or new features. Please ensure that your contributions adhere to the existing code style and include appropriate tests.

## Contributing
This project is licensed under the MIT License. See the LICENSE file for details.