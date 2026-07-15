# SOLID Principles — DIP (Dependency Inversion Principle)

## 1) What DIP says, in plain words

> High-level code should not depend on low-level code directly. Both should depend on an abstraction (an interface). And the abstraction should not depend on the details — the details should depend on the abstraction.

That sentence is confusing on first read, so here's the plain version:

- **High-level code** = the part that does the important business logic (example: `UserService`, which handles user-related work).
- **Low-level code** = the part that does the technical, specific work (example: `MySQLDatabase`, which knows how to talk to MySQL).

Without DIP, your important business logic (`UserService`) is directly tied to one specific technical detail (`MySQLDatabase`). If that detail changes, your important code has to change too — which is backwards. The important code shouldn't have to care about low-level details at all.

**DIP fixes the direction:** instead of `UserService` depending on `MySQLDatabase` directly, both `UserService` and `MySQLDatabase` depend on a shared interface — say, `Database`. Neither one depends on the other directly anymore.

**Common mix-up to avoid:** DIP is not the same thing as "Dependency Injection" (DI). DI — passing an object in from outside instead of creating it inside a class — is just a *technique* often used to achieve DIP. You can use dependency injection and still violate DIP (for example, injecting a concrete `MySQLDatabase` instead of an interface). DIP is about *what type* you depend on. DI is about *how* you get a hold of it.

---

## 2) `Without_DIP` — the problem

Files: `main`, `MongoDatabase`, `MySQLDatabase`, `UserService`

Here, `UserService` directly creates and depends on one specific database class:

```java
public class UserService {
    private MySQLDatabase database = new MySQLDatabase();

    public void saveUser(User user) {
        database.save(user);
    }
}
```

**What's wrong with this:**

- `UserService` is your important business logic. It shouldn't need to know or care whether data is stored in MySQL, MongoDB, or anything else.
- If you want to switch to `MongoDatabase`, you have to open up `UserService` and edit it — even though the actual user-saving logic hasn't changed at all.
- You can't test `UserService` on its own. Since it creates a real `MySQLDatabase` internally, any test of `UserService` also needs a real, working MySQL connection. You can't swap in a fake database for testing.
- If tomorrow you need both MySQL and MongoDB running at once, `UserService` has no clean way to support that — you'd be back to writing `if` checks inside it.

This is the same shape of problem as the OCP example from earlier (`shoppingCartStorage` hardcoded to one database) — and that's not a coincidence. DIP is usually *how* you fix an OCP violation like that one.

---

## 3) `With_DIP` — the fix

Files: `main`, `Database` (interface), `MongoDatabase`, `MySQLDatabase`, `UserService`

```java
public interface Database {
    void save(User user);
}

public class MySQLDatabase implements Database {
    public void save(User user) { /* MySQL-specific code */ }
}

public class MongoDatabase implements Database {
    public void save(User user) { /* MongoDB-specific code */ }
}

public class UserService {
    private final Database database;

    public UserService(Database database) {
        this.database = database;   // handed in from outside, not created here
    }

    public void saveUser(User user) {
        database.save(user);
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Database db = new MySQLDatabase();   // or new MongoDatabase()
        UserService userService = new UserService(db);
        userService.saveUser(new User(...));
    }
}
```

**What changed:**

- `UserService` now depends only on the `Database` interface — not on `MySQLDatabase` or `MongoDatabase` specifically.
- Both `MySQLDatabase` and `MongoDatabase` depend on the same `Database` interface too. Notice the direction: neither the high-level class (`UserService`) nor the low-level classes depend on each other directly — they both point at the shared interface instead. That's the actual "inversion" in the name.
- Switching databases means changing one line in `main` — `UserService` is never touched.
- Testing `UserService` becomes easy: you can hand it a fake `Database` that just records what was saved, with no real database involved at all.

---

## 4) Why this matters

- Your important business logic stays stable even when technical details (which database, which API, which file format) change underneath it.
- It's what makes real unit testing possible — you can swap in fake versions of anything your class depends on.
- It's what actually makes OCP work in practice. OCP says "extend without modifying old code." DIP is the mechanism that makes that possible — without depending on an interface instead of a concrete class, there's no clean way to add a new option (like a new database) without editing existing code.

---

## 5) The direction is the whole point

A common mistake is thinking DIP just means "use interfaces everywhere." That's not quite it. What matters is **which way the dependency arrow points.**

- **Before DIP:** `UserService` → depends on → `MySQLDatabase` (high-level code depends on a low-level detail)
- **After DIP:** `UserService` → depends on → `Database` ← `MySQLDatabase` also depends on `Database`

Both high-level and low-level code now depend on the same abstraction. Neither depends on the other. That's the "inversion" — the low-level detail (`MySQLDatabase`) used to be the thing everything pointed at; now it's just another implementation of a shared interface, and it's the one that has to conform to the abstraction instead of the other way around.

---

## 6) The honest cost — don't apply this everywhere blindly

Same warning as with OCP: don't create an interface for every single class just because DIP exists.

**When DIP is worth it:**
- You genuinely expect more than one implementation (multiple databases, multiple payment providers, multiple notification channels).
- You need to swap in a fake/mock version for testing.
- The dependency is on something likely to change for reasons outside your control (a third-party service, a specific database technology).

**When it's not worth it:**
- A class has exactly one reason to exist and one implementation, with no real chance of a second one ever showing up. Wrapping something like `Math` or a simple internal utility class behind an interface "just in case" adds a layer of indirection with nothing to show for it — more files to open just to trace one method call.

Interfaces aren't free. Every one you add is a hop someone has to follow when reading the code. Add them where the flexibility is real, not as a reflex.

---

## 7) How to spot a violation

- A high-level class creates (`new SomeConcreteClass()`) its own dependencies internally instead of receiving them from outside.
- You can't unit test a class without a real database, real network call, or other real external system.
- Switching a technical detail (database, API provider, file format) requires editing business logic classes, not just configuration.
- A class's constructor or fields reference a specific concrete class instead of an interface.

---

## 8) Interview-ready definition

> DIP states that high-level modules and low-level modules should both depend on abstractions, not on each other directly — and abstractions shouldn't depend on implementation details. In practice: depend on interfaces, not concrete classes, so business logic stays stable when technical details change. It's closely tied to OCP (DIP is usually the mechanism that makes OCP possible) and is often implemented using dependency injection, though DIP and DI are not the same thing.

---

## 9) Quick summary

**Without_DIP:** `UserService` creates and depends on `MySQLDatabase` directly. Switching databases or testing without a real database is difficult.

**With_DIP:** `UserService` depends on the `Database` interface. `MySQLDatabase` and `MongoDatabase` both implement it. Switching databases means changing one line in `main`; `UserService` never changes.

**Real cost:** don't wrap every class in an interface — only where more than one implementation is realistic, or testing genuinely needs it.

## 10) Easy way to remember it

**Depend on the interface, not the implementation — and let the details adjust to the abstraction, not the other way around.**