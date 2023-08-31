import React from 'react';
import ReactDOM from 'react-dom';
import {EnrollmentUI} from './EnrollmentUI';
import {createTheme, loadTheme, mergeStyles, Theme} from '@fluentui/react';
import reportWebVitals from './reportWebVitals';
import {datadogRum} from '@datadog/browser-rum'

import {ddColor} from "./commons/styles.util";

export const ENROLLMENTS_API_URL = process.env.REACT_APP_ENROLLMENTS_API_URL;
export const GRADES_API_URL = process.env.REACT_APP_GRADES_API_URL;
//export const TUITION_API_URL = process.env.REACT_APP_TUITION_API_URL;

// Inject some global styles

mergeStyles({
    ':global(body,html,#root)': {
        margin: 0,
        padding: 0,
        height: '100vh'
    }
});

const enrollmentUITheme: Theme = createTheme({
    palette: {
        themePrimary: ddColor
    }
})

loadTheme(enrollmentUITheme)

ReactDOM.render(<EnrollmentUI/>, document.getElementById('root'));
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
/*
datadogRum.init({
    applicationId: process.env.REACT_APP_DD_RUM_APPLICATION_ID,
    clientToken: process.env.REACT_APP_DD_RUM_CLIENT_ID,
    site: 'datadoghq.com',
    service: 'enrollments-ui',
    env: process.env.REACT_APP_DD_RUM_ENV,
    version: '1.0',
    allowedTracingUrls: ["http://localhost:8080", "http://localhost:8081", "http://localhost:8082"],
    sessionSampleRate: 100,
    sessionReplaySampleRate: 100, // if not included, the default is 100
    trackResources: true,
    trackLongTasks: true,
    trackUserInteractions: true,
});
datadogRum.startSessionReplayRecording();
*/
reportWebVitals();
