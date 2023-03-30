package `is`.hi.hbv601g.taem.Persistance

data class Employee (
    var username : String,
    var accountType : Int,
    var email : String,
    var ssn : String,
    var firstName : String,
    var lastName : String,
    var company : String,
    var jobTitle : String,
    var salary : Double,
    var phoneNumber : String,
    var startDate : String,
    var vacationDaysUsed : Int,
    var sickDaysUsed : Int
    // @TODO finish this @Kristófer Breki
    )


/**
 *     "username": "dori",
"accountType": 0,
"employeeUserName": null,
"email": "hjv6@hi.is",
"firstName": "Halldór Jens",
"lastName": "Vilhjálmsson",
"company": "To be changed",
"jobTitle": "To be changed",
"salary": 0.0,
"phoneNumber": "7918151",
"startDate": "2022-11-27",
"vacationDaysUsed": 0,
"sickDaysUsed": 0,
"remainingVacationDays": 0,
"remainingSickDays": 0,
"firstNameOfEmployee": null,
"lastNameOfEmployee": null,
"phoneNumberEmployee": null,
"ssnEmployee": null,
"status": null,
"ssn": "2911963149",
"accounttype": 0
 */