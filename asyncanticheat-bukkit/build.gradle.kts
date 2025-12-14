// Bukkit module - Paper/Spigot support

tasks.shadowJar {
    // PacketEvents shading guidance:
    // - relocate API package and implementation package to unique namespaces to avoid conflicts
    //   when multiple projects shade PacketEvents.
    relocate("com.github.retrooper.packetevents", "md.thomas.asyncanticheat.bukkit.shaded.packetevents.api")
    relocate("io.github.retrooper.packetevents", "md.thomas.asyncanticheat.bukkit.shaded.packetevents.impl")
    minimize()
}



