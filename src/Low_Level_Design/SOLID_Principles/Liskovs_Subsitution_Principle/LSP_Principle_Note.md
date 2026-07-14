# SOLID Principles — LSP (Liskov Substitution Principle)

> **Note on this document:** the earlier version guessed at what the classes do from screenshots alone. Section 3 below now uses your actual `EmployeeInterfaceSpecialAllowances` interface. `Employee`, `Perks`, `PermanentEmployee`, `TemporaryEmployee`, and `EmployeeManager` bodies are still not confirmed — the surrounding class code in section 3 is written to be consistent with your real interface, not copied from your actual files. Paste those if you want the rest verified line-for-line.

---

## 1) What LSP means (formal version)

> **If B is a subtype of A, objects of type A should be replaceable with objects of type B without altering the correctness of the program.**

The popular phrasing — "a child should behave like the parent" — is too vague to actually test against. The **real test** (from Liskov/Wing's formal definition) has three parts. A subclass violates LSP if it does any of these:

1. **Strengthens preconditions** — requires more from the caller than the parent did (e.g., parent's method accepts any input, child's override throws on inputs the parent accepted).
2. **Weakens postconditions** — guarantees less than the parent promised (e.g., parent guarantees a non-null return, child returns null).
3. **Breaks an invariant** — violates a rule the parent held true for all instances (e.g., parent guarantees `area = width × height` stays consistent after either is set).

If a subclass does none of these, it satisfies LSP — regardless of how different its internal implementation is. If it does any of these, it violates LSP — even if it "sounds like" a valid is-a relationship.

---

## 2) The classic example (Rectangle/Square)

This is worth knowing because it's the standard reference case — a violation that has nothing to do with missing methods, only broken invariants:

```
class Rectangle {
    setWidth(w)  { this.width = w; }
    setHeight(h) { this.height = h; }
    area()       { return this.width * this.height; }
}

class Square extends Rectangle {
    setWidth(w)  { this.width = w; this.height = w; }  // forces both
    setHeight(h) { this.width = h; this.height = h; }  // forces both
}
```

`Square` is a mathematically valid "is-a" Rectangle. But code that does:

```
rect.setWidth(5);
rect.setHeight(10);
assert(rect.area() == 50);
```

works for `Rectangle` and **breaks silently for `Square`** (`area()` returns 100, not 50). No exception, no null — just a wrong answer, because `Square` broke an invariant the caller relied on (`width` and `height` are independent). This is why "does the child have all the parent's methods" is the wrong question — it's whether the child preserves what callers were **already relying on**.

---

## 3) Applying the same test to an Employee/Perks hierarchy

Suppose the base `EmployeeInterface` promises perks and bonus to every employee — something like:

```java
public interface EmployeeInterface {
    Double calculateSalary();
    List<Perks> getPerks();       // promised to every implementer
    Double calculateBonus();      // promised to every implementer
}
```

**Violation shape (`Without_LSP`):**

```java
public class ContractEmployee implements EmployeeInterface {
    public Double calculateSalary() { return baseRate * hoursWorked; }

    public List<Perks> getPerks() {
        throw new UnsupportedOperationException("Contract employees have no perks");
    }

    public Double calculateBonus() {
        throw new UnsupportedOperationException("Contract employees have no bonus");
    }
}
```

Any code written against `EmployeeInterface` — e.g. `EmployeeManager` calling `getPerks()` on a list of employees — works fine for `PermanentEmployee` and **throws at runtime** the moment it hits a `ContractEmployee`. That's a strengthened precondition: the base interface never told callers to expect an exception here.

**The fix (`With_LSP`) — your actual interface:**

```java
public interface EmployeeInterfaceSpecialAllowances {
    Double calculateBonus();
    List<Perks> getPerks();
}
```

`calculateSalary()` stays on the base `EmployeeInterface` — every employee gets paid. Bonus and perks move to `EmployeeInterfaceSpecialAllowances`, a separate contract that **only employee types who actually have these implement**:

```java
public class PermanentEmployee implements EmployeeInterface, EmployeeInterfaceSpecialAllowances {
    public Double calculateSalary() { ... }
    public Double calculateBonus()  { ... }
    public List<Perks> getPerks()   { ... }
}

public class ContractEmployee implements EmployeeInterface {
    public Double calculateSalary() { ... }
    // no calculateBonus(), no getPerks() — and none required, because
    // ContractEmployee never claims to implement EmployeeInterfaceSpecialAllowances
}
```

Now `ContractEmployee` isn't lying about supporting bonus/perks — it simply doesn't claim to. `EmployeeManager` can safely call `calculateSalary()` on any `EmployeeInterface`, and separately call `calculateBonus()`/`getPerks()` only on employees it knows implement `EmployeeInterfaceSpecialAllowances` (e.g. via `instanceof` check or by holding a typed list of just those employees). No runtime surprise, because the type system — not a try/catch — enforces what's actually available.

**This is the key correction to the original doc:** the fix wasn't "give every employee perks," and it wasn't "leave `getPerks()` throwing on the base interface." It was **removing the false promise from the base contract entirely** — `EmployeeInterface` stops claiming every employee has perks, so no implementer has to lie about it.

---

## 4) Why LSP matters

- Polymorphism only works if substitutability is real — code written against the base type must stay correct for every subtype.
- Without it: `instanceof` checks creep in everywhere, callers have to special-case subtypes, and the type hierarchy becomes decoration rather than a guarantee.
- With it: code written once against `Employee` works for every current and future subtype without modification — this is also what makes OCP achievable (you can't safely extend via new subclasses if substitutability isn't guaranteed).

---

## 5) How the fix works — and its real connection to ISP

Splitting `EmployeeInterface` so that `EmployeeInterfaceSpecialAllowances` carries `calculateBonus()`/`getPerks()` separately is **Interface Segregation Principle**, not LSP itself. LSP is *diagnosing* the problem (the base interface makes a promise not every implementer can keep); ISP is *the tool* that resolves it (stop bundling optional behavior into a contract everyone is forced to implement).

**Pattern worth noting:** just like OCP leaned on DIP to work (Persistence example, earlier doc), LSP violations here are fixed using ISP. These principles aren't independent — SOLID is one connected design discipline, not five separate checklists. You'll rarely apply just one in isolation on real code.

---

## 6) How to actually test for a violation

Don't ask "does the child have every parent method." Ask, for each overridden method:
- Does it accept everything the parent's version accepted, or less? (precondition check)
- Does it guarantee everything the parent's version guaranteed, or less? (postcondition check)
- Does it preserve every invariant the parent held? (invariant check)

Concrete red flags:
- Overridden method throws an exception the base method's contract didn't promise
- Overridden method returns `null`/dummy values where the base guaranteed a real value
- Caller needs `instanceof` checks to use a type safely
- A subtype needs special-case handling anywhere it's used polymorphically

---

## 7) Interview-ready definition

> LSP requires that subtypes be substitutable for their base type without altering program correctness. Formally: subclasses can't strengthen preconditions, weaken postconditions, or break invariants the base type guaranteed. Violations often stem from base types promising more than every subtype can honor — commonly fixed by segregating the interface (ISP) so subtypes only implement what they can actually support.

---

## 8) Revision summary

**Violates LSP:** subclass throws on inputs the parent accepted, returns null/dummy where the parent guaranteed a value, or breaks an invariant callers relied on (Rectangle/Square).

**Doesn't violate LSP:** subclass legitimately has less to offer (e.g., no perks) *as long as the base contract never promised that to every subtype* — the fix is narrowing the contract (ISP), not forcing every subtype to fake support.

**Test to apply:** precondition, postcondition, invariant — not "does it have the method."

## 9) Memory trick

**A subclass can do less than what the contract allows — but never less than what the contract promises.**