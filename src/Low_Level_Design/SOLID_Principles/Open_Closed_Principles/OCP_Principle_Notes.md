# SOLID Principles — OCP (Open/Closed Principle)

## 1) What OCP means

> **Software entities (classes, interfaces, methods, modules) should be open for extension but closed for modification.**

You should be able to add new behavior without editing existing, tested code.

**Important clarification — "closed" is scoped, not absolute.**
"Closed for modification" does **not** mean a class is frozen forever. You can still:
- fix bugs in it,
- refactor its internals,
- improve its performance.

What you shouldn't need to do is **change its behavior, or the code that depends on it, just to support a new variant of something it already does.** If adding a fourth database means editing `shoppingCartStorage` again, the design failed at OCP — not because the class was edited, but because the edit was forced by a new *case*, not a new *bug or improvement*.

---

## 2) Why OCP is used

Without it:
- existing code gets modified for every new requirement
- new features can silently break old ones
- if-else / switch chains grow without bound
- regression risk increases with every change

With it:
- new functionality is added via new classes
- existing, tested code is left alone
- easier to test, scale, and maintain

---

## 3) Folder structure

```
SOLID_Principles
└── OCP
    ├── Without_OCP
    └── With_OCP
```

---

## 4) `Without_OCP` — the problem

Files: `main`, `product`, `shoppingCart`, `shoppingCartStorage`

`shoppingCartStorage` has storage logic hardcoded. Supporting MongoDB means editing it directly:

```
if (SQL) ...
else if (Mongo) ...
```

Every new storage type (Redis, Firebase, an API) means another edit, another branch, another risk of breaking the branches already working.

---

## 5) `With_OCP` — the fix

Files: `main`, `product`, `shoppingCart`, `Persistence`, `SQLPersistence`, `MongoPersistence`

| Element | Role |
|---|---|
| `Persistence` | Interface — defines *what* must happen (`saveCart(...)`), not *how* |
| `SQLPersistence` | Implements `Persistence` for SQL only |
| `MongoPersistence` | Implements `Persistence` for MongoDB only |
| `shoppingCart` | Depends on the `Persistence` interface, not a concrete class |
| `main` | Chooses the implementation and injects it into `shoppingCart` |

To add Redis: create `RedisPersistence`, implement `Persistence`. Nothing else changes.

**This is also the Dependency Inversion Principle (DIP) at work.** OCP and DIP aren't separate tricks — here, DIP (`shoppingCart` depends on an abstraction, not a concrete class) is *the mechanism* that makes OCP possible. You can't satisfy OCP in this example without also applying DIP. Worth knowing they usually show up together, not as isolated techniques.

---

## 6) The part most explanations skip: don't do this speculatively

The example makes abstraction look free. It isn't.

If `shoppingCartStorage` has only ever needed SQL, and nobody has asked for Mongo, building `Persistence` + `SQLPersistence` in advance is **not good design — it's a guess.** And guesses about future requirements are usually wrong.

**Cost of abstracting too early:**
- an interface with exactly one implementation, permanently
- extra indirection with no payoff
- code that's harder to read, to save a modification that was never coming
- effort spent designing for flexibility nobody asked for, instead of the feature that was actually requested

**When OCP is worth applying:**
- you already have two or more real implementations, or
- a second one is near-certain and already on the roadmap — not hypothetical

**When it isn't:**
- you have one implementation and no concrete signal a second is coming

Rule of thumb: **write the concrete class first. Introduce the interface when the second real case shows up, not before.** Retrofitting an interface later is cheap. Carrying unused abstraction for months or years is not.

This is the same tradeoff that shows up in SRP (split into more classes vs. fewer) — every SOLID principle trades indirection/files against flexibility. None of them are free wins; they're bets that only pay off if the variation you designed for actually materializes.

---

## 7) Real-world analogy

A phone has one charging port. Different compatible chargers can be used without redesigning the phone — the port is closed for modification, open for extension.

(Analogy holds as-is — no correction needed.)

---

## 8) Problems OCP solves

- Repeated edits to the same class for every new feature
- Long if-else / switch chains keyed on type
- Tight coupling between business logic and specific implementations
- Regression bugs from touching working code
- Difficulty scaling to new cases

---

## 9) Interview-ready definition

> OCP states that software entities should be open for extension but closed for modification — new functionality should be added via new implementations of an abstraction, not by editing existing code. In practice, this depends on DIP: the consuming code must depend on an interface, not a concrete class. Applied prematurely, before real variation exists, it produces unnecessary abstraction — so it's used when a second real case exists or is imminent, not speculatively.

---

## 10) How to spot a violation

- Repeated edits to the same class for every new feature
- Large if-else / switch chains based on type
- Multiple database- or vendor-specific conditions in one class
- One class handling many implementations directly

**And the inverse — over-application:**
- An interface with only one implementation and no second one in sight
- Abstraction layers added "in case we need it later" with no concrete driver

Both are worth catching. One is under-design, the other is wasted effort.

---

## 11) Revision summary

**Without_OCP:** `shoppingCartStorage` handles one storage type directly; every new database means editing it again.

**With_OCP:** `Persistence` defines the contract; `SQLPersistence` and `MongoPersistence` implement it; `shoppingCart` depends on the interface, not a concrete class; new databases are added, not edited in.

**Benefits:** easier extension, stable existing code, lower coupling, better testability.

**Cost (often omitted):** more files, more indirection — worth it only once there's real variation to support.

## 12) Memory trick

**Extend through new implementations, don't modify working code — but don't build the interface until a second implementation is actually real.**