package io.petproject.model

data class Address(val country: String, val streetName: String, val zipCode: String, val city: String, val state: String) {

    class Builder {

        private lateinit var _streetName: String
        private lateinit var _city: String
        private lateinit var _state: String
        private lateinit var _country: String
        private lateinit var _zipCode: String

        fun streetName(streetName: String): Builder {
            this._streetName = streetName.trim()
            return this
        }

        fun city(city: String): Builder {
            this._city = city.trim()
            return this
        }

        fun state(state: String): Builder {
            this._state = state.trim()
            return this
        }

        fun country(country: String): Builder {
            this._country = country.trim()
            return this
        }

        fun zipCode(zipCode: String): Builder {
            this._zipCode = zipCode.trim()
            return this
        }

        fun build(): Address {
            return Address(_country, _streetName, _zipCode, _city, _state)
        }
    }

}