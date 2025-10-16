from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
from langchain_google_genai import GoogleGenerativeAI
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain
import os
import uvicorn

# ----------------------------
# FastAPI App
# ----------------------------
app = FastAPI(title="LangChain AI Agent for Blood Tracking")

# ----------------------------
# Data Models
# ----------------------------
class Donor(BaseModel):
    id: int
    name: str
    bloodGroup: str
    location: str
    contactNumber: str

class DonorData(BaseModel):
    donors: List[Donor]

# ----------------------------
# Setup Google Gemini LLM
# ----------------------------
os.environ["GOOGLE_API_KEY"] = ""

# Initialize LLM
llm = GoogleGenerativeAI(model="text-bison-001")  # safe default

# Setup prompt template
prompt = PromptTemplate(
    input_variables=["donor_info"],
    template=(
        "You are an AI assistant helping analyze blood donation data.\n\n"
        "Here is the donor data:\n{donor_info}\n\n"
        "Generate a summary of insights such as:\n"
        "- Total donors\n"
        "- Which blood group is most common\n"
        "- Which locations need more donors\n"
        "- Suggestions to improve donor engagement\n\n"
        "Give your output in a short, structured and readable way."
    )
)

# Create LLM chain
chain = LLMChain(llm=llm, prompt=prompt)

# ----------------------------
# API Endpoint
# ----------------------------
@app.post("/analyze")
async def analyze_donors(data: DonorData):
    donor_text = "\n".join([f"{d.name} ({d.bloodGroup}) from {d.location}" for d in data.donors])
    try:
        response = chain.run(donor_info=donor_text)
        return {"ai_analysis": response}
    except Exception as e:
        return {"error": str(e)}

# ----------------------------
# Run server
# ----------------------------
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)
