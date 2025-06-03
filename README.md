# Async Anticheat

Multi-platform Minecraft plugin (Paper/Spigot, BungeeCord, Velocity) that captures high-signal packets via PacketEvents and streams them to a cloud service for async analysis.

This project is **GPL-3.0** (required for PacketEvents compatibility).

Packet capture is implemented with [PacketEvents](https://docs.packetevents.com/). The plugin requires the PacketEvents plugin to be installed on each platform.

## Requirements

- Java 21

## Modules

```text
async_anticheat/
├── asyncanticheat-core/     # Shared logic (config, spooling, uploader, models)
├── asyncanticheat-bukkit/   # Paper/Spigot plugin entrypoint
├── asyncanticheat-bungee/   # BungeeCord plugin entrypoint
└── asyncanticheat-velocity/ # Velocity plugin entrypoint
```

## Build

```bash
cd async_anticheat
./gradlew build
```

Output jar: `build/libs/async-anticheat-<version>.jar`

## Configuration

On first start, the plugin generates `config.yml` in its data folder (platform-specific). Example:

```yaml
api:
  url: "http://127.0.0.1:3002"
  token: "change_me"
  timeout_seconds: 10

spool:
  dir: "spool"
  max_mb: 256
  flush_interval_ms: 1000
  drop_policy: "drop_oldest" # drop_oldest | drop_newest

capture:
  # If empty, AsyncAnticheat uses a conservative built-in allow-list (movement/combat/digging/etc).
  enabled_packets: []
  disabled_packets: []
  sample_rate: 1.0

privacy:
  redact_chat: true
```

## Data flow

- Packet capture is implemented with PacketEvents **shaded into the jar**, so no separate PacketEvents plugin is required.
- Packets are buffered in memory, written to gzipped NDJSON batch files under `spool/`, then uploaded to `async_anticheat_api`.
- If the API is unreachable, the plugin enters a **spool-only** mode (temporary) and retries later with exponential backoff.

## Platform differences (Paper/Spigot vs proxies)

AsyncAnticheat runs on **Paper/Spigot (Bukkit)** as well as **Velocity** and **BungeeCord** proxies, but the **available signal differs** depending on where you install it.

In short: **installing on Paper/Spigot provides the richest data and supports the most checks**, while proxy installs are best viewed as **supplemental** capture.

| Capability / signal | Paper/Spigot (Bukkit) | Velocity (proxy) | BungeeCord (proxy) |
|---|---:|---:|---:|
| Player identity (UUID/name attached to records) | ✅ | ✅ | ✅ |
| Serverbound movement fields (position/rotation/on-ground) | ✅ | ⚠️ limited | ⚠️ limited |
| Server-side context (e.g., allow-flight, invulnerable, gamemode) | ✅ | ❌ | ❌ |
| Clientbound entity sync fields (spawn/teleport/relative move) | ✅ | ❌ (not currently extracted) | ❌ (not currently extracted) |
| Inventory interaction fields (clicks/slot changes, etc.) | ✅ | ❌ | ❌ |

### Recommended setups

- **Single-server (Paper/Spigot)**: install `asyncanticheat-bukkit` on the server for best results.
- **Network with a proxy**: for best detection quality, install on the **backend Paper/Spigot servers**. Proxy installs can be useful for proxy-level visibility but do not currently provide the same depth of fields as Bukkit.


