package pl.elpassion.iotguard.commander

sealed class CommanderAction {
    object MoveForward : CommanderAction()
    object MoveBackward : CommanderAction()
    object MoveLeft : CommanderAction()
    object MoveRight : CommanderAction()
    object Stop : CommanderAction()

    data class Connect(val robotAddress: String) : CommanderAction()
}

