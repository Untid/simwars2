# SimWars2

Combat benchmark simulator for Guild Wars 2, inspired by SimulationCraft.
Given a character's stats, a set of skills and a rotation, it computes the expected damage per cycle and the DPS against a target, and it is validated against real in-game combat logs.

> **Status:** early development (`v0.x`). The damage core is working and validated; the web UI, the Spring Boot middleware and the Blish HUD client are not built yet.

## Why this project

The goal is to estimate how much DPS a build can do, and to compare builds (different gear, traits or boons) by running the same engine twice with different inputs. The first milestone is a pure PvE benchmark with static rotations and no human latency.

## Current scope (MVP)

- Profession: Warrior (Core), axe/axe auto-attack chain as the first validated rotation.
- Game mode: PvE only (WvW is planned after PvE is solid).
- Critical hits modeled as an expected value (deterministic, no RNG).
- Stats are provided directly; no gear or trait modifiers yet.

## Architecture

Maven multi-module project, Java 21. The domain has no framework dependencies, and the compiler enforces it: `simwars2-domain` does not declare Spring or Jackson.

```
simwars2/
├── simwars2-domain/          Pure Java: damage model, skills, rotations, ports
└── simwars2-infrastructure/  Adapters (Jackson): GW2 API JSON -> domain objects
```

Domain packages and their dependencies (arrows point to what is used):

```
rotation ──► skill, damage, stats
damage   ──► skill, stats
skill        (no dependencies)
stats        (no dependencies)
```

| Package | Contents |
|---|---|
| `stats` | `StatSheet`, `CritModel` (crit chance and multiplier from Precision and Ferocity) |
| `skill` | `Hit`, `Skill`, `SkillCatalogPort` (port implemented by the infrastructure module) |
| `damage` | `Target`, `DamageCalculator`, `SkillDamage`, `CritResolver` and `ExpectedValueCritResolver` |
| `rotation` | `Rotation`, `RotationDamage` (cycle damage and DPS) |

Design choices:

- **Immutable value objects.** Records with validation in the compact constructor.
- **Pure functions** for the damage maths, so they are trivially testable.
- **Hexagonal ports.** The domain defines `SkillCatalogPort`; `Gw2JsonSkillCatalog` adapts the GW2 API format to it, expanding `hit_count` into individual hits.
- **`CritResolver` as a strategy,** so switching from expected value to Monte Carlo means adding an implementation, not changing the engine.

## Validation against the game

The model was checked against an arcdps log (6 min 42 s on a 1M-health golem, Power 2556, Precision 1961, Ferocity 1186, golem armor 2597, weapon strength 900-1100).

| Check | Model | Game log |
|---|---|---|
| Chop, minimum hit (normal, min weapon strength) | 620 | 620 |
| Chop, maximum hit (crit, max weapon strength) | 1735 | 1735 |
| Crit chance / crit damage from the hero panel | 50.76 % / 229 % | 50.76 % / 229 % |
| Full axe chain, average damage | about 8634 | about 8690 |
| DPS of the chain | about 2398 | 2414 |

The remaining difference (below 1 %) is within the sampling noise of the observed crit rate over roughly 690 hits.

## Getting started

Requirements: JDK 21 and Maven (the bundled Maven of IntelliJ works).

```
mvn clean test
```

Run it from the repository root so the reactor builds `domain` before `infrastructure`. The CI workflow runs the same command on every push and pull request to `main` and `develop`.

## Roadmap

- [x] Base damage and crit model validated against logs
- [x] Skills with per-hit coefficients, rotations and DPS
- [x] Skill catalog port and GW2 JSON adapter
- [ ] Damage modifiers (Vulnerability, Might, traits), validated one at a time
- [ ] Stat pipeline (gear, runes, sigils to `StatSheet`)
- [ ] Spring Boot middleware with SQLite and Flyway
- [ ] Web UI (Thymeleaf + HTMX)
- [ ] Blish HUD client (C#)
- [ ] WvW mode

## Branching

`main` holds tagged milestones, `develop` is the integration branch, and work happens in `feature/*`, `refactor/*`, `chore/*` and `docs/*` branches merged through pull requests.

## Disclaimer

SimWars2 is an unofficial, non-commercial fan project. It is not affiliated with, endorsed or sponsored by ArenaNet or NCSOFT. It does not automate or modify the game client.

© ArenaNet LLC. All rights reserved. NCSOFT, ArenaNet, Guild Wars, Guild Wars 2, GW2, Heart of Thorns, Path of Fire, End of Dragons, Secrets of the Obscure, Janthir Wilds, Visions of Eternity, and all associated logos, designs, and composite marks are trademarks or registered trademarks of NCSOFT Corporation. All other trademarks are the property of their respective owners.