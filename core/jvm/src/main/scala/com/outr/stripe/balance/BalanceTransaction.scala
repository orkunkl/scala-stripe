package com.outr.stripe.balance

import com.outr.stripe.Money
import com.outr.stripe.transfer.SourcedTransfers

case class BalanceTransaction(id: String,
                              `object`: String,
                              amount: Money,
                              availableOn: Long,
                              created: Long,
                              currency: String,
                              description: Option[String],
                              fee: Money,
                              feeDetails: List[FeeDetail],
                              net: Money,
                              source: String,
                              sourcedTransfers: SourcedTransfers,
                              status: String,
                              `type`: String)
