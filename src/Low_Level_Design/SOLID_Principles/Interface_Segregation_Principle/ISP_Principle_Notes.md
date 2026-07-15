# SOLID Principles — ISP (Interface Segregation Principle)

## 1) What ISP means

> **"Clients should not be forced to depend upon interfaces that they do not use."** — Robert C. Martin

Simple version: a class shouldn't have to implement methods it can't meaningfully support, just because they live on the same interface as methods it does need.

**Important nuance the simple version hides:** ISP is defined from the **client's** side, not just the implementer's. It's not only "don't make Rectangle implement `calculateVolume()`" — it's "don't make *any code that calls Rectangle* depend on a `calculateVolume()` it never uses." In languages with compiled interfaces, this has a concrete cost: a client can end up needing to recompile or redeploy because an unrelated method on a fat interface changed — even though the client never called that method. Segregation avoids that coupling, not just avoids empty method bodies.

---

## 2) Why ISP is needed

One large interface with unrelated methods forces every implementer to support all of them, even where it makes no sense. This produces:
- empty or dummy method bodies
- `UnsupportedOperationException` thrown from methods that "exist" but shouldn't
- classes that are hard to read because their real capability is buried in noise
- clients coupled to methods they never call

ISP fixes this by splitting one fat interface into several focused ones, so each implementer — and each client — only deals with what it actually uses.

---

## 3) Folder structure

```
SOLID_Principles
└── ISP
    ├── Without_ISP
    │      ├── main, Shape, Rectangle, Square, Cube
    └── With_ISP
           ├── main, Rectangle, Cube, TwoDimensionalShape, ThreeDimensionalShape
```

---

## 4) `Without_ISP` — the problem

One `Shape` interface declares both `calculateArea()` and `calculateVolume()`.

- `Rectangle` and `Square` are 2D — they can implement `calculateArea()` but have no meaningful `calculateVolume()`. Forced to implement it anyway, usually as a dummy return or a thrown exception.
- `Cube` is 3D — volume makes sense, but it's stuck on the same interface as shapes that can't support it, so the interface itself misrepresents what every implementer can actually do.

**Concrete cost:** any code that only needs `calculateArea()` (billing a client by floor area, say) is still coupled to an interface carrying `calculateVolume()`. If `calculateVolume()`'s signature changes, that unrelated client is affected too — not because it uses volume, but because it depends on the same fat interface.

---

## 5) `With_ISP` — the fix

```java
interface TwoDimensionalShape   { double calculateArea(); }
interface ThreeDimensionalShape { double calculateVolume(); }

class Rectangle implements TwoDimensionalShape {
    public double calculateArea() { ... }
}

class Cube implements ThreeDimensionalShape {
    public double calculateVolume() { ... }
}
```

`Rectangle` only implements `TwoDimensionalShape`. `Cube` only implements `ThreeDimensionalShape`. No dummy methods, no thrown exceptions, and — importantly — a client that only needs area is only coupled to `TwoDimensionalShape`, nothing else.

**Worth noticing:** `Square` and `Rectangle` here are independent implementers of `TwoDimensionalShape`, not one extending the other. That's not incidental — the classic Square/Rectangle example is the standard *LSP* violation (see the LSP doc): if `Square` had extended `Rectangle` and overridden `setWidth`/`setHeight` to keep both equal, you'd break the invariant callers relied on. Segregating by interface instead of forcing an inheritance relationship sidesteps that trap entirely. ISP and LSP problems often show up in the same badly-modeled hierarchy.

---

## 6) ISP vs. SRP — they are not the same principle

They sound similar ("don't force unnecessary stuff on a class") but operate on different axes:

| | SRP | ISP |
|---|---|---|
| Unit of concern | A class | An interface / contract |
| The question it asks | Does this class have more than one reason to change (more than one actor)? | Is any client forced to depend on methods it doesn't use? |
| Violation looks like | One class doing unrelated jobs for different stakeholders | One interface bundling methods only some implementers/clients need |

**A class can satisfy SRP and still violate ISP.** A `Shape` class that does exactly one job — "represent a shape's geometry" — can still expose a fat interface if that one job happens to bundle area and volume together. SRP is about the class's *purpose*; ISP is about the *shape of the contract* clients see.

---

## 7) Real-world example (the sharper one)

Robert Martin's own example is closer to ISP than a school analogy: a `MultifunctionDevice` interface with `print()`, `scan()`, `fax()`. A simple printer-only machine is forced to implement `scan()` and `fax()` it can't perform — usually by throwing or silently doing nothing. Split into `Printer`, `Scanner`, `Fax`, and each device implements only what it actually does.

**Secondary analogy (weaker, but not wrong):** a school forcing every teacher to teach every subject. This is closer to SRP in spirit (one person doing unrelated jobs) — useful for intuition, but don't rely on it to explain ISP specifically. The printer example is the one to reach for if asked "what is ISP" precisely.

---

## 8) What problem ISP solves

Fat interfaces cause:
- implementers writing methods they can't meaningfully support
- clients coupled to methods they never call, creating unnecessary rebuild/redeploy dependencies
- unclear class contracts — you can't tell what a class actually does by looking at what it implements

ISP fixes this by segregating interfaces around what specific clients actually need — not just around natural object categories. Two 2D shapes might still warrant separate interfaces if different clients use disjoint subsets of their behavior; "put all 2D stuff together" is a reasonable default, not the rule itself.

---

## 9) Interview-ready definition

> ISP states that no client should be forced to depend on methods it doesn't use. Fat interfaces are split into smaller, role-specific ones based on what different clients actually need — not just by natural object category. It's distinct from SRP: SRP governs why a class changes; ISP governs what a client is coupled to.

---

## 10) How to identify a violation

- An interface has methods only some implementers use.
- Implementers have empty method bodies or throw `UnsupportedOperationException`.
- A client only calls 2 of an interface's 8 methods but is coupled to all 8.
- Changing an unrelated method forces unrelated clients to rebuild/retest.

---

## 11) The honest cost — over-segregation

**This is the part usually left out, and it's a real cost, not a footnote.** Splitting too aggressively produces interface explosion: a class implementing 6 tiny single-method interfaces is harder to reason about than one implementing 2 well-scoped ones, because there's no single place to see "what can this object do." More files, more indirection, more cognitive overhead tracing which interface a call came from.

**When segregation is worth it:** implementers are being forced into empty/dummy methods, or distinct clients demonstrably use disjoint subsets of a fat interface.

**When it isn't:** every implementer uses every method anyway — splitting further adds files without removing any real coupling.

---

## 12) Revision summary

**Without_ISP:** one `Shape` interface bundles `calculateArea()` and `calculateVolume()`; 2D shapes are forced to fake support for volume.

**With_ISP:** `TwoDimensionalShape` and `ThreeDimensionalShape` are separate; each class and each client depends only on what it actually needs.

**Distinct from SRP:** SRP is about class purpose/actors; ISP is about client-facing contract shape.

**Real cost:** over-segregation creates interface explosion — apply based on actual disjoint usage, not preemptively.

## 13) Memory trick

**Don't make a client see methods it will never call.**