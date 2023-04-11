package `is`.hi.hbv601g.taem.Persistance

import `is`.hi.hbv601g.taem.Networking.SessionUser

data class ViewTransactionUserDAO (
    var sessionUser: SessionUser,
    var totalHours : Double,
    var transactionList : List<Transaction>
        ){
}