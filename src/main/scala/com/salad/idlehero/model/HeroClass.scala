package com.salad.idlehero.model

import com.salad.idlehero.model.ItemSlots.ItemSlot

case class HeroClass(name: String,
                     itemSlots: Seq[ItemSlot])
