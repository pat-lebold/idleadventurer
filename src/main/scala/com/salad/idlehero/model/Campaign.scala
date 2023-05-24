package com.salad.idlehero.model

case class Campaign(name: String,
                    elementDistribution: Map[Element, Double],
                    stages: Seq[Stage])
