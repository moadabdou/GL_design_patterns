# Issue: Facade Pattern — Unified Export Interface

## Description
Implement the Facade pattern to provide a simplified, unified interface for document exporting. The `DocumentExporterFacade` hides the complexity of format selection, element rendering, PDF adaptation, and file I/O behind a single clean API.

## Objectives
- Create `DocumentExporterFacade` that simplifies the export workflow
- The facade orchestrates rendering, format-specific conversion, and output writing
- Provide a single `export(Document, String filePath, ExportStrategy strategy)` method

## Tasks
- [ ] Create `facade/DocumentExporterFacade.java` with:
  - `export(Document document, String filePath, ExportStrategy strategy)` — exports using the given strategy
  - `exportPDF(Document document, String filePath)` — convenience method for PDF export
  - `exportHTML(Document document, String filePath)` — convenience method for HTML export
  - `exportMarkdown(Document document, String filePath)` — convenience method for Markdown export
- [ ] The facade handles:
  - Rendering document elements into a unified string representation
  - Delegating format-specific conversion to the provided `ExportStrategy`
  - Writing the output to the specified file path
  - Logging export operations via the `Logger` singleton
- [ ] Write unit tests verifying:
  - `export()` calls the correct strategy
  - Convenience method `exportPDF()` works end-to-end (strategy + adapter)
  - Facade properly logs export operations
  - Facade handles errors gracefully (file not found, write permission denied)

## Acceptance Criteria
- External clients (including `main()`) interact only with `DocumentExporterFacade` for all export needs
- The facade delegates format-specific logic to strategies without exposing strategy internals
- Adding a new export format does not require changing the facade

## Pattern Reference
- **Facade** (Structural): Provides a unified interface to a set of interfaces in a subsystem. Defines a higher-level interface that makes the subsystem easier to use.

## Technical Notes
- The facade will integrate with the Adapter (for PDF) and Strategy (for format selection) patterns
- The facade should not contain business logic — it delegates to appropriate subsystems
- Future enhancement: add progress notification via Observer pattern for long exports
