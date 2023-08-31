import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import './EnrollmentUI.css';
import { Home } from './Home/Home';
import {StudentUpsertPage} from "./Students/StudentUpsertPage/StudentUpsertPage";
import {Students} from "./Students/Students";
import {Courses} from "./Courses/Courses";

export class EnrollmentUI extends React.Component {

    render() {
        return (
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={ <Home /> }/>
                    <Route path='/students' element={ <Students /> }/>
                    <Route path='/students/upsert/:id' element={ <StudentUpsertPage /> }/>
                    <Route path='/students/upsert' element={ <StudentUpsertPage /> }/>
                    <Route path='/courses' element={ <Courses /> }/>
                </Routes>
            </BrowserRouter>
        );
    }

}
