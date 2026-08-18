from sqlalchemy import Column, Integer, String, Float, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime
from app.database import Base

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    full_name = Column(String(100))
    email = Column(String(100), unique=True, index=True)
    phone = Column(String(20))
    institution = Column(String(100))
    hashed_password = Column(String(255))
    role = Column(String(50)) # LAB_HEAD or RESEARCH_SCHOLAR
    samples = relationship("Sample", back_populates="scholar")

class Sample(Base):
    __tablename__ = "samples"
    id = Column(Integer, primary_key=True, index=True)
    sample_id = Column(String(50), unique=True, index=True)
    scholar_id = Column(Integer, ForeignKey("users.id"))
    sample_type = Column(String(100))
    volume = Column(Float)
    number_of_samples = Column(Integer)
    depositor_name = Column(String(100))
    institution = Column(String(100))
    contact_number = Column(String(20))
    email_id = Column(String(100))
    date_of_deposit = Column(String(50))
    storage_options = Column(String(100))
    experiment_details = Column(String(500))
    status = Column(String(50), default="DRAFT") # DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, RESULT_SENT
    rejection_reason = Column(String(500), nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)
    
    scholar = relationship("User", back_populates="samples")
    processing = relationship("Processing", back_populates="sample", uselist=False)

class Processing(Base):
    __tablename__ = "processing"
    id = Column(Integer, primary_key=True, index=True)
    sample_id = Column(Integer, ForeignKey("samples.id"))
    processing_date = Column(String(50))
    samples_processed = Column(Integer)
    process_conducted = Column(String(200))
    storage_conditions = Column(String(100))
    notes = Column(String(500))
    
    sample = relationship("Sample", back_populates="processing")
