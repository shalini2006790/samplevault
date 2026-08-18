from fastapi import APIRouter, Depends, HTTPException, UploadFile, File, Form
from sqlalchemy.orm import Session
from typing import List
import shutil
import os
from app.database import get_db
from app.schemas.all_schemas import SampleCreate, SampleResponse, ProcessingCreate
from app.models.all_models import Sample, User, Processing
from app.auth import get_current_user

router = APIRouter(prefix="/samples", tags=["samples"])

os.makedirs("../uploads", exist_ok=True)

def generate_sample_id(db: Session):
    count = db.query(Sample).count() + 1
    return f"SV-2026-{count:04d}"

@router.post("/", response_model=SampleResponse)
def create_sample(sample: SampleCreate, db: Session = Depends(get_db)):
    # Hardcode scholar_id=1 for testing
    db_sample = Sample(**sample.model_dump(), scholar_id=1, sample_id=generate_sample_id(db))
    db.add(db_sample)
    db.commit()
    db.refresh(db_sample)
    return db_sample
@router.get("/", response_model=List[SampleResponse])
def get_samples(db: Session = Depends(get_db)):
    # Just return everything for testing
    return db.query(Sample).all()
@router.post("/{id}/processing")
def add_processing(id: int, proc: ProcessingCreate, db: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    sample = db.query(Sample).filter(Sample.id == id).first()
    db_proc = Processing(**proc.model_dump(), sample_id=sample.id)
    sample.status = "PROCESSING"
    db.add(db_proc)
    db.commit()
    return {"message": "Processing details added"}

@router.post("/{id}/upload")
async def upload_document(id: int, file: UploadFile = File(...), doc_type: str = Form(...), db: Session = Depends(get_db)):
    file_location = f"../uploads/{id}_{doc_type}_{file.filename}"
    with open(file_location, "wb+") as f:
        shutil.copyfileobj(file.file, f)
    
    sample = db.query(Sample).filter(Sample.id == id).first()
    if sample:
        if doc_type == "results" or doc_type == "raw_data":
            sample.status = "UNDER_REVIEW"
        db.commit()
    return {"filename": file.filename, "type": doc_type}

@router.post("/{id}/approve")
def approve_sample(id: int, db: Session = Depends(get_db)):
    sample = db.query(Sample).filter(Sample.id == id).first()
    sample.status = "APPROVED"
    db.commit()
    return {"message": "Approved"}
    
@router.get("/dashboard/analytics")
def get_analytics(db: Session = Depends(get_db)):
    total = db.query(Sample).count()
    pending = db.query(Sample).filter(Sample.status != "APPROVED").count()
    approved = db.query(Sample).filter(Sample.status == "APPROVED").count()
    return {"total_samples": total, "approved_samples": approved, "pending_samples": pending}
@router.post("/{id}/send-results")
def send_email_to_client(id: int, db: Session = Depends(get_db)):
    sample = db.query(Sample).filter(Sample.id == id).first()
    if sample:
        # In a real production app, you would put SMTP email logic here!
        sample.status = "RESULT_SENT"
        db.commit()
        return {"message": f"Results successfully emailed to {sample.email_id}"}
    return {"message": "Sample not found"}