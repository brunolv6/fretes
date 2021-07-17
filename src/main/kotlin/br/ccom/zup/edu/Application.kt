package br.ccom.zup.edu

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.ccom.zup.edu")
		.start()
}

