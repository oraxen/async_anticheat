package md.thomas.asyncanticheat.core;

/**
 * Reasons why a player might be exempted from packet capture.
 * 
 * <p>
 * Based on analysis of NoCheatPlus, AAC5, and Vulcan exemption systems:
 * <ul>
 * <li>NCP: MovingListener early returns,
 * MovingUtil.shouldCheckSurvivalFly()</li>
 * <li>AAC: iS exemption manager (creative, spectator, sleeping, dead,
 * floodgate)</li>
 * <li>Vulcan: Global ignore rules, grace windows, Floodgate/Geyser
 * detection</li>
 * </ul>
 * 
 * <p>
 * Note: NPC checks are omitted because NPCs don't send packets.
 */
public enum ExemptionReason {

    // ============= GAMEMODE EXEMPTIONS =============
    // All three anticheats handle these specially
    // AAC: iS.r (creative), iS.l (spectator)
    // NCP: MovingUtil.shouldCheckSurvivalFly() checks gamemode

    /**
     * Player is in Spectator mode.
     * Spectators can fly through blocks and have no collision.
     * All major anticheats exempt this mode.
     */
    SPECTATOR_MODE,

    /**
     * Player is in Creative mode.
     * Creative players have different physics (instant break, flight).
     * AAC/NCP exempt; useful to still capture but label differently.
     */
    CREATIVE_MODE,

    // ============= PLAYER STATE EXEMPTIONS =============
    // AAC: iS.i (sleeping), iS.b (dead)
    // NCP: MovingListener.onPlayerMove() early returns

    /**
     * Player is dead.
     * No useful movement data - player cannot move.
     */
    DEAD,

    /**
     * Player is sleeping in a bed.
     * Player is immobilized - no useful movement data.
     */
    SLEEPING,

    /**
     * Player is inside a vehicle.
     * Vehicle movement uses completely different physics.
     * NCP uses separate VehicleChecks for this.
     */
    IN_VEHICLE,

    // ============= FLIGHT EXEMPTIONS =============
    // AAC: checks.move.check_flying config toggle
    // NCP: player.isFlying(), player.getAllowFlight()
    // Vulcan: flight_cooldown (40 ticks after /fly)

    /**
     * Player is currently flying (creative flight or plugin-granted).
     * Uses different movement rules than survival movement.
     */
    FLYING,

    /**
     * Player is gliding with an elytra.
     * Elytra flight has completely different physics.
     */
    ELYTRA_GLIDING,

    // ============= BEDROCK/GEYSER EXEMPTIONS =============
    // AAC: iS.g (Floodgate detection on init)
    // Vulcan: ignore-floodgate, ignore-geyser-uuids, ignore-geyser-prefixes
    // This is CRITICAL - Bedrock has fundamentally different movement physics

    /**
     * Player is a Bedrock client (via Floodgate/Geyser).
     * Bedrock has different movement physics that would cause false positives.
     * Both AAC and Vulcan exempt these players by default.
     */
    BEDROCK_CLIENT,

    // ============= GRACE PERIOD EXEMPTIONS =============
    // NCP: Various grace periods for unreliable data
    // Vulcan: join-check-wait-time (2500ms), min-ticks-existed (3)
    // Vulcan: max-velocity-ticks (50), flight-cooldown (40)

    /**
     * Player just joined the server.
     * Initial position data may be unreliable.
     * Vulcan uses 2500ms grace period.
     */
    JUST_JOINED,

    /**
     * Player just respawned.
     * Position resets - data during transition is unreliable.
     */
    JUST_RESPAWNED,

    /**
     * Player just teleported.
     * Awaiting client position confirmation - data is unreliable.
     */
    JUST_TELEPORTED,

    /**
     * Player just changed worlds.
     * World transition resets movement state.
     */
    WORLD_CHANGE,

    /**
     * Player recently received external velocity (knockback, explosion, etc.).
     * Movement during this window is unpredictable.
     * Vulcan uses 50 ticks (2.5 seconds) grace.
     */
    VELOCITY_GRACE,

    /**
     * Player recently stopped flying.
     * Transition period from flight to survival movement.
     * Vulcan uses 40 ticks (2 seconds) cooldown.
     */
    FLIGHT_COOLDOWN,

    // ============= POTION EFFECT EXEMPTIONS =============
    // NCP: Bridge1_9.getLevitationAmplifier() checks
    // These significantly alter movement physics

    /**
     * Player has Levitation potion effect.
     * Dramatically alters vertical movement physics.
     */
    LEVITATION_EFFECT,

    /**
     * Player has Slow Falling potion effect.
     * Alters fall physics significantly.
     */
    SLOW_FALLING_EFFECT
}
