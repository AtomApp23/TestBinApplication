package ru.atomapp.testbinapplication.main.domain

abstract class DomainException : IllegalStateException()

class NoInternetConnectionException : DomainException()

class ServiceUnavailableException: DomainException()