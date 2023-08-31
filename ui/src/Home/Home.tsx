import React from "react";
import {Footer} from "../Footer/Footer";
import {Header} from "../Header/Header";
import {NavigationBar} from "../commons/NavigationBar/NavigationBar";

export const Home = () => {

    return (
        <div>
            <Header/>
            <NavigationBar />
            <div>
                Home Content Here
            </div>
            <Footer/>
        </div>
    );
}