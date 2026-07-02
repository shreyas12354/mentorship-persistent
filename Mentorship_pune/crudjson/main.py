
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI()

# Predefined JSON Data
students = [
    {"id": 1, "name": "Shreyas", "course": "Computer Engineering"},
    {"id": 2, "name": "Rahul", "course": "AI & ML"}
]

# Request Model
class Student(BaseModel):
    id: int
    name: str
    course: str

# READ - Get all students
@app.get("/students")
def get_students():
    return students

# READ - Get one student
@app.get("/students/{student_id}")
def get_student(student_id: int):
    for student in students:
        if student["id"] == student_id:
            return student
    raise HTTPException(status_code=404, detail="Student not found")

# CREATE
@app.post("/students")
def add_student(student: Student):
    students.append(student.dict())
    return {"message": "Student added successfully"}

# UPDATE
@app.put("/students/{student_id}")
def update_student(student_id: int, updated_student: Student):
    for index, student in enumerate(students):
        if student["id"] == student_id:
            students[index] = updated_student.dict()
            return {"message": "Student updated successfully"}

    raise HTTPException(status_code=404, detail="Student not found")

# DELETE
@app.delete("/students/{student_id}")
def delete_student(student_id: int):
    for index, student in enumerate(students):
        if student["id"] == student_id:
            students.pop(index)
            return {"message": "Student deleted successfully"}

    raise HTTPException(status_code=404, detail="Student not found")