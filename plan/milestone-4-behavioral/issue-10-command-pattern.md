# Issue: Command Pattern — Undo/Redo Operations

## Description
Implement the Command pattern to encapsulate document operations (add element, remove element, modify style) as objects, enabling robust undo/redo functionality. A `CommandHistory` manager tracks executed commands.

## Objectives
- Define `Command` interface with `execute()` and `undo()` methods
- Implement concrete commands: `AddElementCommand`, `RemoveElementCommand`, `ModifyStyleCommand`
- Create `CommandHistory` to manage undo/redo stack
- Commands store enough state to fully reverse their operation

## Tasks
- [ ] Create `behavioral/Command.java` — interface with:
  - `void execute()`
  - `void undo()`
  - `String getDescription()` — for UI/logging display
- [ ] Create `behavioral/AddElementCommand.java`:
  - Constructor takes `Document` and `Element`
  - `execute()` calls `document.addElement(element)`
  - `undo()` calls `document.removeElement(element)`
  - Stores the index where element was inserted for correct undo positioning
- [ ] Create `behavioral/RemoveElementCommand.java`:
  - Constructor takes `Document` and index
  - `execute()` removes element at index and stores it internally
  - `undo()` re-inserts the element at the stored index
- [ ] Create `behavioral/ModifyStyleCommand.java`:
  - Constructor takes `Element`, old decorator, new decorator
  - `execute()` applies new decorator
  - `undo()` reverts to old decorator
- [ ] Create `behavioral/CommandHistory.java`:
  - Two stacks: `undoStack` and `redoStack`
  - `executeCommand(Command cmd)` — executes and pushes to undo stack, clears redo stack
  - `undo()` — pops from undo stack, calls `undo()`, pushes to redo stack
  - `redo()` — pops from redo stack, calls `execute()`, pushes to undo stack
  - `canUndo()`, `canRedo()` — query methods
- [ ] Write unit tests verifying:
  - `execute()` followed by `undo()` restores document to original state
  - Multiple undo/redo operations work correctly in sequence
  - New command after undo clears the redo stack
  - `AddElementCommand` correctly re-inserts at original position
  - `RemoveElementCommand` correctly re-inserts the removed element

## Acceptance Criteria
- Every document modification is reversible
- Undo/redo stack depth is limited only by memory (configurable cap optional)
- Commands store sufficient snapshot state for correct reversal
- Command history handles edge cases (undo on empty history, redo after new command)

## Pattern Reference
- **Command** (Behavioral): Turns a request into a stand-alone object that contains all information about the request. This transformation lets you parameterize methods with different requests, delay or queue a request's execution, and support undoable operations.

## Technical Notes
- `ModifyStyleCommand` demonstrates undo/redo for decorator changes (cross-pattern integration with Decorator)
- For `AddElementCommand`, store the insertion index in `execute()` rather than computing it in the constructor
- Consider adding a command description for debugging/logging purposes
