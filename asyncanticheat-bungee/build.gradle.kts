// Bungee module - BungeeCord support

tasks.shadowJar {
    relocate("com.github.retrooper.packetevents", "md.thomas.asyncanticheat.bungee.shaded.packetevents.api")
    relocate("io.github.retrooper.packetevents", "md.thomas.asyncanticheat.bungee.shaded.packetevents.impl")
    minimize()
}



