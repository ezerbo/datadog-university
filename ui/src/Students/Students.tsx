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
import {Header} from "../Header/Header";
import {Footer} from "../Footer/Footer";
import {NavigationBar} from "../commons/NavigationBar/NavigationBar";
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

    const navigate = useNavigate();

    let timeoutID = null;
    const selection: Selection = new Selection({
        onSelectionChanged: () => {
            let selections = selection.getSelection();
            setState({selected: selections.length ? selections[0] as Student : null});
        }
    });

    useEffect(() => {
        getAll();
        return () => {
            clearTimeout(timeoutID);
        }
    }, []);

    const getAll = () => {
        setState({gettingStudents: true});
        axios({
            url: `${ENROLLMENTS_API_URL}/students`,
            method: 'GET',
            headers: {'Accept': 'application/json'}
        })
            .then(res => handleErrors(res))
            .then(res => {
                setState({students: res.data, gettingStudents: false});
                props.onCountChange(res.data.length)
            }).catch(error => {
            console.log(JSON.stringify(error));
            setState({gettingStudents: false});
        });
    }

    const getEnrollments = (studentId: number) => {
        setState({enrollments: undefined});
        axios({
            url: `${ENROLLMENTS_API_URL}/students/${studentId}/enrollments`,
            method: 'GET',
            headers: {'Accept': 'application/json'}
        })
            .then(res => handleErrors(res))
            .then(res => {
                setState({enrollments: res.data.enrollments});
            }).catch(error => {
            console.log(JSON.stringify(error));
        });
    }

    const dismissDialog = () => {
        setState({showDialog: false})
    }

    const toggleConfirmationDialog = () => {
        setState({showConfirmationDialog: !studentState.showConfirmationDialog});
    }

    const toggleEnrollmentsDialog = () => {
        if (!studentState.showEnrollmentsDialog) { // State is changing from 'hidden' to 'shown'
            getEnrollments(studentState.selected.id);
        }
        setState({showEnrollmentsDialog: !studentState.showEnrollmentsDialog})
    }

    const deleteStudent = () => {
        toggleConfirmationDialog();
        setStudentState(studentState => ({...studentState,
            ...{processing: true, processingMessage: 'Removing a student...'}}));
        axios({
            url: `${ENROLLMENTS_API_URL}/students/${studentState.selected.id}`,
            method: 'DELETE'
        })
            .then(() => {
                selection.setAllSelected(false);
                setState({
                    processing: false,
                    selected: null,
                    message: 'Student successfully removed',
                    messageType: MessageBarType.success,
                    students: studentState.students.filter(student => student.id !== studentState.selected.id)
                });
                resetMessage();
            })
            .catch(error => {
                console.log(JSON.stringify(error));
                setState({
                    processing: false,
                    message: 'Student could not be removed',
                    messageType: MessageBarType.error
                })
                resetMessage();
            });
    }

    const onEnroll = (e: Enrollment) => {
        studentState.enrollments.push(e);
        setState({enrollments: studentState.enrollments});
        resetMessage();
    }

    const onUnenroll = (enrollment: Enrollment) => {
        setState({enrollments: studentState.enrollments.filter(e => e.gradeId !== enrollment.gradeId)
                .slice()});
        resetMessage();
    }

    const onCreate = (student: Student) => {
        studentState.students.push(student);
        props.onCountChange(studentState.students.length);
        setState({students: studentState.students.slice(), message: 'Student Created Successfully'});
        resetMessage();
    }

    const onEdit = (student: Student) => {
        let index = studentState.students.findIndex(c => c.id === student.id);
        studentState.students[index] = student;
        setState({ students: studentState.students.slice(), message: 'Student Updated Successfully'});
        resetMessage();
    }

    const resetMessage = () => {
        timeoutID = setTimeout(() => setState({message: null}), 5000);
    }

    const setState = (state: any) => {
        setStudentState(studentState => ({...studentState, ...state}));
    }

    return (
        <div>
            <Header/>
            <NavigationBar />
            <div style={{paddingLeft: 10}}>
                {
                    studentState.message && (
                        <div style={{padding: 5}}>
                            <Message
                                message={studentState.message}
                                type={studentState.messageType}
                            />
                        </div>
                    )
                }
                {
                    studentState.processing && (
                        <Spinner label={studentState.processingMessage} labelPosition="bottom"/>
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
                        enableShimmer={studentState.gettingStudents}
                        columns={columns}
                        selectionMode={SelectionMode.single}
                        setKey="students"
                        layoutMode={DetailsListLayoutMode.justified}
                        selection={selection}
                        selectionPreservedOnEmptyClick={true}
                    />
                </div>
                <StudentDialog
                    show={studentState.showDialog}
                    student={studentState.editing ? studentState.selected : undefined}
                    onCreateSuccess={onCreate}
                    onEditSuccess={onEdit}
                    onDismiss={dismissDialog}
                />
                {
                    studentState.showConfirmationDialog && (
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
                                enrollments={studentState.enrollments}
                                show={studentState.showEnrollmentsDialog}
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
        }
    }
];