package com.canonicalexamples.jobgenius.model

data class JobFacts(
    var id: String = "",
    var type: String = "",
    var url: String = "",
    var created_at: String = "",
    var company: String = "",
    var company_url: String = "",
    var location: String = "",
    var title: String = "",
    var description: String = "",
    var how_to_apply: String = "",
    var company_logo: String = ""
)
