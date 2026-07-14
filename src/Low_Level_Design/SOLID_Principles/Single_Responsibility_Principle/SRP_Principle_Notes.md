# SOLID Principles — SRP (Single Responsibility Principle)

## 1) What SRP actually means

The common definition — "one class, one job" — is a simplification. It's useful for beginners but wrong in a way that causes bad decisions once you use it on real code.

The actual principle (from Robert C. Martin, who coined SRP):

> **A class should have only one reason to change.**

"Reason to change" does **not** mean "one function." It means **one actor** — one person, team, or stakeholder whose needs the class serves.

**Why the distinction matters:**
A class can look like it does "one job" and still violate SRP if two different stakeholders can force changes to it independently.

Example: a `ShoppingCart` that calculates the total price. That's "one job" on paper. But:
- Finance can change how tax is calculated.
- Product/marketing can change discount logic.

Two different actors, two different reasons to change, same class. That's an SRP violation — even though "job = calculate total" sounds singular.

**Rule of thumb:** don't ask "does this class do one thing?" Ask "**who** would ask me to change this class, and could two different people ask for unrelated reasons?"

---

## 2) Why this principle is used

SRP prevents mixed responsibilities — a class that serves multiple actors at once.

If one class handles business logic, printing, storage, formatting, and validation, then a change requested by one stakeholder risks breaking behavior another stakeholder depends on. That produces:
- tight coupling
- poor maintainability
- low readability
- harder testing
- more bugs from unrelated changes

SRP fixes this by separating concerns so each class answers to one actor.

---

## 3) Folder structure

- `SOLID_principles` — main folder, one subfolder per principle.
- `SRP` — contains two subfolders: `Without_SRP` and `Without_SRP` (bad design) vs `With_SRP` (fixed design). Good teaching structure: shows the problem, then the fix, side by side.

---

## 4) `Without_SRP` — the problem

Files: `main`, `product`, `shoppingCart`

Here, `shoppingCart` likely does several unrelated things — stores items, calculates totals, prints details, and maybe persists data. This isn't just "too many methods" — it's serving multiple actors (a display/UI stakeholder, a business-logic stakeholder, a storage stakeholder) in one class.

**Consequence:** a change requested by any one of those stakeholders touches a class the others depend on. That's how unrelated features start breaking each other.

---

## 5) `With_SRP` — the fix

Files: `main`, `product`, `shoppingCart`, `shoppingCartPrinter`, `shoppingCartStorage`

| Class | Responsibility | Answers to |
|---|---|---|
| `product` | Models product data (name, price, quantity, id) | Domain/data shape |
| `shoppingCart` | Cart logic — add, remove, calculate totals | Business/domain rules |
| `shoppingCartPrinter` | Formats and displays cart info | Presentation/UI needs |
| `shoppingCartStorage` | Persists cart data (file, DB, etc.) | Infrastructure/storage needs |
| `main` | Entry point — wires objects together | N/A (composition root) |

Each class now serves one actor. A UI change never touches storage code, and vice versa.

---

## 6) Benefit — and its limit

**Benefit:** if printing changes, only `shoppingCartPrinter` changes. If storage changes, only `shoppingCartStorage` changes. This reduces regression risk and makes each class easier to read, test, and reuse.

**Limit — don't over-apply this.** Splitting classes for every conceivable future change creates class explosion: dozens of tiny classes for trivial logic, which hurts readability just as much as a god-class does. Split when there are **actual, currently-distinct reasons to change** — not preemptively for hypothetical ones. SRP is a judgment call, not a mechanical rule you apply maximally.

---

## 7) Real-world analogy (corrected)

A chef, a waiter, and a cashier each do a different job — but the reason SRP applies isn't just "different tasks." It's that **different people manage each role for different reasons**:
- The health inspector cares about the chef's process.
- The floor manager cares about the waiter's service.
- Finance cares about the cashier's transactions.

If one person did all three, a change demanded by the health inspector (food safety) could disrupt how payments are handled — unrelated concerns tangled together. That's the actual failure mode SRP prevents, not just "one person doing too much."

---

## 8) What SRP solves

- **Low cohesion** — a class with unrelated responsibilities, serving multiple actors.
- **High coupling** — many unrelated features depending on the same class, so any change risks side effects elsewhere.

SRP improves both by aligning each class to one actor.

---

## 9) Interview-ready definition

> SRP states that a class should have only one reason to change — meaning it should serve only one actor or stakeholder. This reduces coupling, improves cohesion, and limits the blast radius of any single change. It applies to modules and services, not just classes.

---

## 10) How to spot a violation

- The class name contains "and," or is vague (`Manager`, `Handler`, `Processor`).
- It has methods that would be changed by different people for unrelated reasons.
- It mixes business logic with formatting, I/O, or persistence.
- You can't describe its purpose in one sentence without "and."

**Caveat:** this is a judgment call, not a mechanical test. Two competent engineers can reasonably disagree on where the line sits — that's normal, not a sign the principle is broken.

---

## 11) Scope beyond classes

SRP isn't limited to classes. It applies at every level of a codebase:
- **Function** — one function, one job.
- **Class** — one actor, one reason to change.
- **Module/package** — one cohesive concern.
- **Service (microservices)** — one bounded business capability.

The "actor" test scales up: at the service level, ask "which team owns changes to this service?"

---

## 12) Revision summary

**Without SRP:** one class serves multiple actors — a change for one stakeholder risks breaking another's code.

**With SRP:** each class serves one actor:
- `product` → data shape
- `shoppingCart` → business rules
- `shoppingCartPrinter` → presentation
- `shoppingCartStorage` → persistence
- `main` → composition

**Main benefit:** smaller blast radius per change, at the cost of more files to navigate — a tradeoff, not a free win.

---

## 13) Memory trick (corrected)

**One class, one actor, one reason to change.**

(Not "one job" — a class can have one job and still answer to two masters.)