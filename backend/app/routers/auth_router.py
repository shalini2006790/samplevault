from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from app.database import get_db
from app.schemas.all_schemas import UserCreate, UserResponse, Token
from app.models.all_models import User
from app.auth import get_password_hash, verify_password, create_access_token
from fastapi.security import OAuth2PasswordRequestForm

router = APIRouter(prefix="/auth", tags=["auth"])

@router.post("/register", response_model=UserResponse)
def register(user: UserCreate, db: Session = Depends(get_db)):
    if db.query(User).filter(User.email == user.email).first():
        raise HTTPException(status_code=400, detail="Email already registered")
    if user.role == "LAB_HEAD" and db.query(User).filter(User.role == "LAB_HEAD").first():
        raise HTTPException(status_code=403, detail="Lab Head already exists. Contact Admin.")
    hashed_pwd = get_password_hash(user.password)
    db_user = User(full_name=user.full_name, email=user.email, phone=user.phone, institution=user.institution, role=user.role, hashed_password=hashed_pwd)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

@router.post("/login", response_model=Token)
def login(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    user = db.query(User).filter(User.email == form_data.username).first()
    if not user or not verify_password(form_data.password, user.hashed_password):
        raise HTTPException(status_code=401, detail="Invalid credentials")
    access_token = create_access_token(data={"sub": str(user.id), "role": user.role})
    return {"access_token": access_token, "token_type": "bearer", "role": user.role, "user_id": user.id}
