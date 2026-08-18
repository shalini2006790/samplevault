from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from fastapi.responses import FileResponse
from app.database import engine, Base
from app.routers import auth_router, samples_router

Base.metadata.create_all(bind=engine)

app = FastAPI(title="SampleVault Pro API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(auth_router.router)
app.include_router(samples_router.router)

app.mount("/assets", StaticFiles(directory="../web/assets"), name="assets")
app.mount("/css", StaticFiles(directory="../web/css"), name="css")
app.mount("/js", StaticFiles(directory="../web/js"), name="js")
app.mount("/web", StaticFiles(directory="../web"), name="web")

@app.get("/")
def read_index(): return FileResponse("../web/index.html")
@app.get("/{page}.html")
def read_pages(page: str): return FileResponse(f"../web/{page}.html")
