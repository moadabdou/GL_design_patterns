# Issue: Adapter Pattern — Third-Party PDF Library Integration

## Description
Implement the Adapter pattern to adapt a simulated third-party PDF library (`com.thirdparty.pdf.PDFGenerator`) to the application's `Exporter` interface. This demonstrates how to integrate incompatible external APIs without modifying them.

## Objectives
- Define an `Exporter` interface used by the application
- Create a mock 3rd-party PDF library with an incompatible API
- Implement `PDFLibraryAdapter` that adapts the 3rd-party API to the `Exporter` interface

## Tasks
- [ ] Create `structural/Exporter.java` — interface with:
  - `void export(Document document, OutputStream output)`
- [ ] Create the mock 3rd-party library at `com/thirdparty/pdf/PDFGenerator.java` with:
  - Constructor: `PDFGenerator(String filePath)`
  - Method: `void generatePDF(String content)` — writes to file (incompatible with our `Exporter`)
  - Method: `void setHeader(String header)`
  - Method: `void setFooter(String footer)`
- [ ] Create `structural/PDFLibraryAdapter.java` that:
  - Implements `Exporter`
  - Internally creates and configures a `PDFGenerator`
  - Converts `Document` content to plain text/HTML and passes it to `PDFGenerator.generatePDF()`
- [ ] Write unit tests verifying:
  - `PDFLibraryAdapter.export()` successfully calls the adapted library
  - The adapter correctly translates `Document` content into the format expected by `PDFGenerator`
  - The adapter handles edge cases (empty document, null output stream)

## Acceptance Criteria
- The 3rd-party `PDFGenerator` class is never modified
- All PDF export goes through `PDFLibraryAdapter` which implements `Exporter`
- The rest of the application only depends on the `Exporter` interface, not on `PDFGenerator` directly

## Pattern Reference
- **Adapter** (Structural): Allows objects with incompatible interfaces to collaborate. Converts the interface of a class into another interface clients expect.

## Technical Notes
- The mock `PDFGenerator` writes to a file; the adapter should write to the provided `OutputStream` by using a temporary file or piped streams
- This pattern is a **Object Adapter** (composition-based) — the adapter holds a reference to the adaptee
- Integration: This adapter will be used by the Facade and Strategy patterns later
