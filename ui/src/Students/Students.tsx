import React, {useEffect, useState} from "react";
import axios from "axios";
import {handleErrors, toDate} from "../commons/http.util";
import {ENROLLMENTS_API_URL} from "../index";
import {Student} from "./Student";
import {TableControl} from "../TableControl/TableControl";
import {StudentDialog} from "./StudentDialog/StudentDialog";
import {Enrollment} from "./Enrollment";
import {EnrollmentsDialog} from "./EnrollmentsDialog/EnrollmentsDialog";
import {Message} from "../commons/Message/Message";
import {Confirmation} from "../commons/Confirmation/Confirmation";
import {dangerColor, ddColor} from "../commons/styles.util";
import {useNavigate} from "react-router-dom";

import {
    ActionButton,
    DetailsListLayoutMode,
    IColumn,
    ITooltipHostStyles,
    MessageBarType,
    Selection,
    SelectionMode,
    ShimmeredDetailsList,
    Spinner,
    TooltipHost
} from "@fluentui/react";
import {Header} from "../Header/Header";
import {Footer} from "../Footer/Footer";
import {NavigationBar} from "../commons/NavigationBar/NavigationBar";

const hostStyles: Partial<ITooltipHostStyles> = {root: {display: 'inline-block'}};
const calloutProps = {gapSpace: 0};

export const Students = (props) => {

    const [studentState, setStudentState] = useState({
        students: [],
        showDialog: false,
        gettingStudents: false,
        showEnrollmentsDialog: false,
        selected: null,
        message: null,
        messageType: null,
        processing: false,
        processingMessage: null,
        editing: false,
        showConfirmationDialog: false,
        enrollments: null
    });
    // const [students, setStudents] = useState([]);
    const [showDialog, setShowDialog] = useState(false);
    const [gettingStudents, setGettingStudents] = useState(false);
    const [showEnrollmentsDialog, setShowEnrollmentsDialog] = useState(false);
    //  const [selected, setSelected] = useState(null);
    const [message, setMessage] = useState(null);
    const [messageType, setMessageType] = useState(null);
    const [processing, setProcessing] = useState(false);
    const [processingMessage, setProcessingMessage] = useState(null);
    const [editing, setEditing] = useState(false);
    const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
    const [enrollments, setEnrollments] = useState(null);

    const navigate = useNavigate();

    let timeoutID = null;
    const selection: Selection = new Selection({
        onSelectionChanged: () => {
            let selections = selection.getSelection();
            let selected = selections.length ? selections[0] as Student : null;
            setStudentState(studentState => ({...studentState, ...{selected: selected}}))
        }
    });

    useEffect(() => {
        getAll();
        return () => {
            clearTimeout(timeoutID);
        }
    }, []);

    const getAll = () => {
        setGettingStudents(true);
        axios({
            url: `${ENROLLMENTS_API_URL}/students`,
            method: 'GET',
            headers: {'Accept': 'application/json'}
        })
            .then(res => handleErrors(res))
            .then(res => {
                setStudentState(studentState => ({...studentState, ...{students: res.data}}));
                setGettingStudents(false);
                props.onCountChange(res.data.length)
            }).catch(error => {
            console.log(JSON.stringify(error));
            setGettingStudents(false);
        });
    }

    const getEnrollments = (studentId: number) => {
        setEnrollments(undefined);
        axios({
            url: `${ENROLLMENTS_API_URL}/students/${studentId}/enrollments`,
            method: 'GET',
            headers: {'Accept': 'application/json'}
        })
            .then(res => handleErrors(res))
            .then(res => {
                setEnrollments(res.data.enrollments);
            }).catch(error => {
            console.log(JSON.stringify(error));
        });
    }

    const dismissDialog = () => {
        setShowDialog(false);
    }

    const toggleConfirmationDialog = () => {
        setShowConfirmationDialog(!showConfirmationDialog);
    }

    const toggleEnrollmentsDialog = () => {
        if (!showEnrollmentsDialog) { // State is changing from 'hidden' to 'shown'
            getEnrollments(studentState.selected.id);
        }
        setShowEnrollmentsDialog(!showConfirmationDialog);
    }

    const deleteStudent = () => {
        toggleConfirmationDialog();
        setProcessing(true);
        setProcessingMessage('Removing a student...')
        axios({
            url: `${ENROLLMENTS_API_URL}/students/${studentState.selected.id}`,
            method: 'DELETE'
        })
            .then(() => {
                selection.setAllSelected(false);
                setProcessing(false);
                setMessage('Student successfully removed');
                setMessageType(MessageBarType.success);
                setStudentState(studentState => ({
                    ...studentState, ...{
                        selected: null,
                        students: studentState.students.filter(student => student.id !== studentState.selected.id)
                    }
                }));
                resetMessage();
            })
            .catch(error => {
                console.log(JSON.stringify(error));
                setProcessing(false);
                setMessage('Student successfully removed');
                setMessageType(MessageBarType.error);
                resetMessage();
            });
    }

    const onEnroll = (e: Enrollment) => {
        enrollments.push(e);
        setEnrollments(enrollments);
        resetMessage();
    }

    const onUnenroll = (enrollment: Enrollment) => {
        setEnrollments(enrollments.filter(e => e.gradeId !== enrollment.gradeId).slice());
        resetMessage();
    }

    const onCreate = (student: Student) => {
        studentState.students.push(student);
        props.onCountChange(studentState.students.length);
        setStudentState(studentState => ({
            ...studentState, ...{
                students: studentState.students.slice(),
                message: 'Student Created Successfully'
            }
        }));
        resetMessage();
    }

    const onEdit = (student: Student) => {
        let index = studentState.students.findIndex(c => c.id === student.id);
        studentState.students[index] = student;
        setStudentState(studentState => ({
            ...studentState, ...{
                students: studentState.students.slice(),
                message: 'Student Updated Successfully'
            }
        }));
        resetMessage();
    }

    const resetMessage = () => {
        timeoutID = setTimeout(() => setMessage(null), 5000);
    }

    return (
        <div>
            <Header/>
            <NavigationBar />
            <div style={{paddingLeft: 10}}>
                {
                    message && (
                        <div style={{padding: 5}}>
                            <Message
                                message={message}
                                type={messageType}
                            />
                        </div>
                    )
                }
                {
                    processing && (
                        <Spinner label={processingMessage} labelPosition="bottom"/>
                    )
                }

                <div style={{paddingTop: 10, float: "left"}}>
                    <TableControl
                        selection={studentState.selected}
                        disabledUntilSelection={!studentState.selected}
                        onAddClick={() => navigate('/students/upsert')}
                        onEditClick={() => navigate(`/students/upsert/${studentState.selected.id}`)}
                        onDeleteClick={toggleConfirmationDialog}
                    />
                </div>
                {
                    studentState.selected && (
                        <div style={{paddingTop: 10, paddingBottom: 10, float: "right"}}>
                            <TooltipHost
                                content="Show Enrollments"
                                id={"enrollments_btn"}
                                calloutProps={calloutProps}
                                styles={hostStyles}
                            >
                                <ActionButton
                                    text="Enrollments"
                                    iconProps={{iconName: 'Education', color: ddColor}}
                                    onClick={toggleEnrollmentsDialog}
                                />
                            </TooltipHost>
                        </div>
                    )
                }
                <div style={{clear: "both"}}>
                    <ShimmeredDetailsList
                        items={studentState.students || []}
                        enableShimmer={gettingStudents}
                        columns={columns}
                        selectionMode={SelectionMode.single}
                        setKey="students"
                        layoutMode={DetailsListLayoutMode.justified}
                        selection={selection}
                        selectionPreservedOnEmptyClick={true}
                    />
                </div>
                <StudentDialog
                    show={showDialog}
                    student={editing ? studentState.selected : undefined}
                    onCreateSuccess={onCreate}
                    onEditSuccess={onEdit}
                    onDismiss={dismissDialog}
                />
                {
                    showConfirmationDialog && (
                        <Confirmation
                            question={'Are you sure you want to remove this student?'}
                            confirmationBtnTxt={'Remove'}
                            onConfirmation={deleteStudent}
                            onDismiss={toggleConfirmationDialog}
                            width={500}
                            confirmationIconName={'Delete'}
                            confirmationBtnStyle={{backgroundColor: dangerColor}}
                        />
                    )
                }
                {
                    studentState.selected && (
                        <div>
                            <EnrollmentsDialog
                                student={studentState.selected}
                                enrollments={enrollments}
                                show={showEnrollmentsDialog}
                                onDismiss={toggleEnrollmentsDialog}
                                onEnroll={onEnroll}
                                onUnenroll={onUnenroll}
                            />
                        </div>
                    )
                }
            </div>
            <Footer/>
        </div>
    );
}

const columns: IColumn[] = [
    {
        key: 'firstname',
        name: 'First name',
        ariaLabel: 'First Name',
        fieldName: 'firstName',
        minWidth: 200,
        maxWidth: 300,
        styles: {root: {color: ddColor}}
    },
    {
        key: 'lastName',
        name: 'Last name',
        ariaLabel: 'Last Name',
        fieldName: 'lastName',
        minWidth: 200,
        maxWidth: 300,
        styles: {root: {color: ddColor}}
    },
    {
        key: 'emailAddress',
        name: 'Email Address',
        ariaLabel: 'Email Address',
        fieldName: 'emailAddress',
        minWidth: 200,
        maxWidth: 300,
        styles: {root: {color: ddColor}}
    },
    {
        key: 'ssn',
        name: 'SSN',
        ariaLabel: 'SSN',
        fieldName: 'ssn',
        minWidth: 200,
        maxWidth: 300,
        styles: {root: {color: ddColor}}
    },
    {
        key: 'dob',
        name: 'Date of Birth',
        ariaLabel: 'Date of Birth',
        fieldName: 'dob',
        minWidth: 200,
        maxWidth: 300,
        styles: {root: {color: ddColor}},
        onRender: (student: Student) => {
            return <span>{toDate(student.dob)}</span>;
        },
    }
];