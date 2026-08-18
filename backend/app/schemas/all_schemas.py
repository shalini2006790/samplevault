from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class UserCreate(BaseModel):
    full_name: str
    email: str
    phone: str
    institution: str
    password: str
    role: str

class UserResponse(BaseModel):
    id: int
    full_name: str
    email: str
    role: str
    class Config: from_attributes = True

class Token(BaseModel):
    access_token: str
    token_type: str
    role: str
    user_id: int

class SampleCreate(BaseModel):
    sample_type: str
    volume: float
    number_of_samples: int
    depositor_name: str
    institution: str
    contact_number: str
    email_id: str
    date_of_deposit: str
    storage_options: str
    experiment_details: str

class ProcessingCreate(BaseModel):
    processing_date: str
    samples_processed: int
    process_conducted: str
    storage_conditions: str
    notes: str

class SampleResponse(SampleCreate):
    id: int
    sample_id: str
    scholar_id: int
    status: str
    rejection_reason: Optional[str]
    created_at: datetime
    class Config: from_attributes = True
