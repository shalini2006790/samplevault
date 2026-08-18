package com.samplevault.models

data class Sample(
    val id: Int,
    val sample_id: String, // <--- WE ADDED THIS LINE!
    val scholar_id: Int,
    val sample_type: String,
    val depositor_name: String,
    val status: String,
    val date_of_deposit: String
)
data class SampleCreate(
    val sample_type: String,
    val volume: Double,
    val number_of_samples: Int,
    val depositor_name: String,
    val institution: String,
    val contact_number: String,
    val email_id: String,
    val date_of_deposit: String,
    val storage_options: String,
    val experiment_details: String
)

data class Analytics(
    val total_samples: Int,
    val approved_samples: Int,
    val pending_samples: Int
)