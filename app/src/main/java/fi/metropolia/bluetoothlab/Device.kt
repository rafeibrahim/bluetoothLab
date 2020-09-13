package fi.metropolia.bluetoothlab

class Device (var name: String,
              var address: String,
              var strength: Int? = 0,
              var isConnetable: Boolean) {

        override fun toString() : String {
            return "$name    $address    $strength dBm"
        }
    }
