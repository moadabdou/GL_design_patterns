# Issue: Strategy Pattern — Interchangeable Export Formats

## Description
Implement the Strategy pattern to enable interchangeable export formats. Different strategies encapsulate format-specific rendering logic (PDF, HTML, Markdown), allowing the client to select export format at runtime.

## Objectives
- Define `ExportStrategy` interface
- Implement `PDFExportStrategy`, `HTMLExportStrategy`, `MarkdownExportStrategy`
- Strategies are interchangeable and can be selected at runtime

## Tasks
- [ ] Create `behavioral/ExportStrategy.java` — interface with:
  - `String render(Document document)` — converts document to format-specific string
- [ ] Create `behavioral/PDFExportStrategy.java` — converts document to a formatted string suitable for PDF generation (used with the PDFAdapter internally)
- [ ] Create `behavioral/HTMLExportStrategy.java` — converts document to HTML string with appropriate tags
  - `<h1>` for title, `<p>` for paragraphs, `<img>` for images, `<table>` for tables
  - Apply decorator formatting to HTML output (bold → `<b>`, italic → `<i>`, color → `<span style="color:...">`)
- [ ] Create `behavioral/MarkdownExportStrategy.java` — converts document to Markdown
  - `# ` for title, plain text for paragraphs, `![alt](path)` for images, pipe tables for tables
  - Apply decorator formatting: `**bold**`, `*italic*`, etc.
- [ ] Write unit tests verifying:
  - Each strategy produces correctly formatted output
  - `HTMLExportStrategy` produces valid HTML (title in `<h1>`, elements in appropriate tags)
  - `MarkdownExportStrategy` produces valid Markdown
  - `PDFExportStrategy` produces the expected intermediate format

## Acceptance Criteria
- Strategies are fully interchangeable — client selects strategy without knowing its implementation details
- Adding a new export format requires only a new strategy class implementing `ExportStrategy`
- Strategies correctly render all element types (text, image, table) and their decorators

## Pattern Reference
- **Strategy** (Behavioral): Defines a family of algorithms, encapsulates each one, and makes them interchangeable. Lets the algorithm vary independently from clients that use it.

## Technical Notes
- Strategies should traverse the document using the Iterator pattern to access elements
- HTML/Markdown strategies should respect decorators by checking `instanceof` or by calling `render()` which already includes formatting
- The Facade will delegate to Strategy; the Strategy for PDF will use the Adapter internally
