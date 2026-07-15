# SOLID Principles — LSP (Liskov Substitution Principle)

## 1) What LSP says, in plain words

> If B is a child type of A, you should be able to use a B object anywhere an A object is expected — and nothing should break.

The common short version — "a child should behave like its parent" — is too fuzzy to actually check against. Here is a clearer, testable way to check it.

**A child class breaks LSP if it does any of these three things:**

1. **Asks for more than the parent did.**
   Example: the parent's method accepts any input. The child's version throws an error on some input the parent was fine with.

2. **Promises less than the parent did.**
   Example: the parent's method always returns a real value. The child's version sometimes returns `null` instead.

3. **Breaks a rule the parent always kept true.**
   Example: the parent guarantees that `area = width × height` stays correct no matter what. The child changes this rule secretly.

If a child class avoids all three, it follows LSP — no matter how different its code looks inside.
If it does even one of these, it breaks LSP — even if it still sounds like a normal "is-a" relationship on paper.

---

## 2) The classic example: Rectangle and Square

This example is famous because the problem has nothing to do with missing methods. It's about breaking a hidden rule.

```
class Rectangle {
    setWidth(w)  { this.width = w; }
    setHeight(h) { this.height = h; }
    area()       { return this.width * this.height; }
}

class Square extends Rectangle {
    setWidth(w)  { this.width = w; this.height = w; }  // changes both
    setHeight(h) { this.width = h; this.height = h; }  // changes both
}
```

On paper, a square is a rectangle. But look what happens:

```
rect.setWidth(5);
rect.setHeight(10);
assert(rect.area() == 50);
```

This works fine with a normal `Rectangle`. But if `rect` is actually a `Square`, the answer becomes 100, not 50 — with no error, no warning, just a silently wrong result.

**Why this happens:** `Square` broke a rule the caller was quietly relying on — that changing the width doesn't also change the height. That hidden rule is called an "invariant" (something that should always stay true). So the real question isn't "does the child have the same methods as the parent?" It's "does the child keep every promise the parent already made?"

---

## 3) Same idea, applied to your Employee code

Say the base interface `EmployeeInterface` promises perks and a bonus to every employee:

```java
public interface EmployeeInterface {
    Double calculateSalary();
    List<Perks> getPerks();       // promised to everyone
    Double calculateBonus();      // promised to everyone
}
```

**The problem version (`Without_LSP`):**

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

Any code written to work with `EmployeeInterface` — say, `EmployeeManager` calling `getPerks()` on a list of employees — works fine until it reaches a `ContractEmployee`, and then it crashes. The base interface never warned anyone this could happen. That's the violation: the child is asking more of the caller (be ready to catch an error) than the parent ever did.

**The fix (`With_LSP`) — using your actual interface:**

```java
public interface EmployeeInterfaceSpecialAllowances {
    Double calculateBonus();
    List<Perks> getPerks();
}
```

`calculateSalary()` stays on the base interface, since every employee gets paid. Bonus and perks move to a separate interface that only the employees who actually have them will implement:

```java
public class PermanentEmployee implements EmployeeInterface, EmployeeInterfaceSpecialAllowances {
    public Double calculateSalary() { ... }
    public Double calculateBonus()  { ... }
    public List<Perks> getPerks()   { ... }
}

public class ContractEmployee implements EmployeeInterface {
    public Double calculateSalary() { ... }
    // no calculateBonus(), no getPerks() — and that's fine, because
    // ContractEmployee never claims to have them
}
```

Now `ContractEmployee` isn't pretending to support bonuses or perks — it simply doesn't sign up for that contract. `EmployeeManager` can safely call `calculateSalary()` on any employee. If it needs bonus/perks info, it only asks employees that actually implement `EmployeeInterfaceSpecialAllowances`.

**The key fix wasn't** "give every employee perks" and it wasn't "keep the error, just document it better." **The actual fix was removing the false promise from the base interface** — so no class is ever forced to lie about what it can do.

---

## 4) Why this matters

- Code that works with the base type should keep working no matter which specific subtype it actually gets. That's the entire point of using shared types in the first place.
- Without LSP: you end up sprinkling `if (employee instanceof ContractEmployee)` checks everywhere, because you can't trust the base type's promises anymore.
- With LSP: you can add brand-new employee types later, and old code that uses `EmployeeInterface` keeps working without any changes. This is also what makes OCP (adding new code without editing old code) actually possible — you can't safely add new subtypes if substitution isn't safe to begin with.

---

## 5) How this connects to ISP

Splitting the bonus/perks methods into their own interface is really the **Interface Segregation Principle** at work, not LSP itself.

Think of it this way:
- **LSP finds the problem:** the base interface is promising something not every class can deliver.
- **ISP provides the fix:** stop bundling optional features into a contract everyone is forced to sign.

These principles aren't separate tools you pick one at a time — in real code, they usually show up together. Same pattern as OCP, which relies on DIP (using interfaces instead of concrete classes) to actually work.

---

## 6) A simple way to test any subclass

Don't ask: *"Does the child have every method the parent has?"*

Instead ask, for every method the child overrides:

- Does it accept everything the parent's version accepted — or does it reject more? (asks more than the parent did)
- Does it guarantee everything the parent's version guaranteed — or does it guarantee less? (promises less than the parent did)
- Does it keep every rule the parent always kept true? (breaks a hidden rule)

**Warning signs to look for:**
- A method throws an error the base version never warned about
- A method returns `null` or a placeholder where the base version always returned something real
- Your code needs `instanceof` checks to safely use an object
- One specific subtype always needs special handling wherever it's used

---

## 7) Short definition for interviews

> LSP means any subtype should be usable anywhere its base type is expected, without breaking the program. In practice: a subclass shouldn't ask more of the caller, promise less than the base type, or break a rule the base type always kept. This usually comes from a base type promising more than every subtype can actually deliver — fixed by splitting the interface (ISP) so each subtype only implements what it truly supports.

---

## 8) Quick summary

**Breaks LSP:**
- Subclass throws an error on input the parent accepted
- Subclass returns `null`/placeholder where the parent always returned something real
- Subclass breaks a rule the parent always kept (Rectangle/Square)

**Doesn't break LSP:**
- Subclass genuinely has less to offer (like no perks) — **as long as the base interface never promised that to everyone in the first place.** The fix is narrowing the promise (ISP), not forcing every subtype to fake support for it.

**The real test:** does it ask more, promise less, or break a rule? — not "does it have the method."

## 9) Easy way to remember it

**A subclass can offer less than what's allowed — but never less than what was promised.**