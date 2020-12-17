package github.afezeria.hymn.common.constant

/**
 * @author afezeria
 */
enum class TriggerEvent {
    BEFORE_INSERT,
    BEFORE_UPDATE,
    BEFORE_UPSERT,
    BEFORE_DELETE,
    AFTER_INSERT,
    AFTER_UPDATE,
    AFTER_UPSERT,
    AFTER_DELETE;
}