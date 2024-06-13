
sealed interface PathInput {
    data object OpenPickerDialog : PathInput
    data object None : PathInput

    companion object {
        fun resolveFrom(path: String): PathInput {
            return when {
                path.startsWith("pickerDialog") -> OpenPickerDialog
                else -> None
            }
        }
    }
}
