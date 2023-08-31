import React from "react";
import axios from "axios";
import {ENROLLMENTS_API_URL} from "../index";
import {handleErrors, toDate} from "../commons/http.util";
import {Enrollment} from "../Students/Enrollment";
import {
    DetailsListLayoutMode,
    IColumn,
    MessageBarType,
    Selection,
    SelectionMode,
    ShimmeredDetailsList,
    Spinner
} from "@fluentui/react";
import {Course} from "./Course";
import {Message} from "../commons/Message/Message";
import {TableControl} from "../TableControl/TableControl";
import {dangerColor, ddColor} from "../commons/styles.util";
import {Confirmation} from "../commons/Confirmation/Confirmation";
import {CourseDialog} from "./CourseDialog/CourseDialog";
import {Header} from "../Header/Header";
import {NavigationBar} from "../commons/NavigationBar/NavigationBar";
import {Footer} from "../Footer/Footer";

interface CoursesState {
    courses: Course[];
    enrollments?: Enrollment[];
    selected?: Course;
    showDialog: boolean;
    showConfirmationDialog?: boolean;
    message?: string;
    messageType?: MessageBarType
    gettingCourses: boolean;
    gettingEnrollments?: boolean;
    editing?: boolean;
    processing?: boolean;
    processingMessage?: string;
}

interface CoursesProps {
    onCountChange?: (count: number) => void
}

export class Courses extends React.Component<CoursesProps, CoursesState> {

    private timeoutID = null;
    private readonly selection: Selection;

    constructor(props: CoursesProps) {
        super(props);
        this.state = {courses: [], showDialog: false, gettingCourses: false};
        this.selection = new Selection({
            onSelectionChanged: () => {
                let selections = this.selection.getSelection();
                let selected = selections.length ? selections[0] as Course : null;
                this.setState({
                    selected: selected
                });
            }
        });
    }

    componentDidMount() {
        this.getAll();
    }

    render() {
        const {
            editing,
            showDialog,
            courses,
            selected,
            message,
            messageType,
            processingMessage,
            processing,
            gettingCourses,
            showConfirmationDialog
        } = this.state;
        return (
            <div>
                <Header/>
                <NavigationBar/>
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
                            selection={selected}
                            disabledUntilSelection={!selected}
                            onAddClick={() => this.showDialog(false)}
                            onEditClick={() => this.showDialog(true)}
                            onDeleteClick={this.toggleConfirmationDialog}
                        />
                    </div>
                    <div style={{clear: "both"}}>
                        <ShimmeredDetailsList
                            items={courses || []}
                            enableShimmer={gettingCourses}
                            columns={this.columns}
                            selectionMode={SelectionMode.single}
                            setKey="students"
                            layoutMode={DetailsListLayoutMode.justified}
                            selection={this.selection}
                            selectionPreservedOnEmptyClick={true}
                        />
                    </div>
                    <CourseDialog
                        show={showDialog}
                        course={editing ? selected : undefined}
                        onCreateSuccess={this.onCreate}
                        onEditSuccess={this.onEdit}
                        onDismiss={this.dismissDialog}
                    />
                    {
                        showConfirmationDialog && (
                            <Confirmation
                                question={'Are you sure you want to remove this course?'}
                                confirmationBtnTxt={'Remove'}
                                onConfirmation={this.deleteCourse}
                                onDismiss={this.toggleConfirmationDialog}
                                width={500}
                                confirmationIconName={'Delete'}
                                confirmationBtnStyle={{backgroundColor: dangerColor}}
                            />
                        )
                    }
                </div>
                <Footer />
            </div>
        );
    }

    private getAll = () => {
        this.setState({gettingCourses: true})
        axios({
            url: `${ENROLLMENTS_API_URL}/courses`,
            method: 'GET',
            headers: {'Accept': 'application/json'}
        })
            .then(res => handleErrors(res))
            .then(res => {
                this.setState(() => {
                    return {courses: res.data, gettingCourses: false};
                });
                this.props.onCountChange(res.data.length)
            }).catch(error => {
            console.log(JSON.stringify(error));
            this.setState({
                gettingCourses: false
            });
        });
    }

    private deleteCourse = () => {
        this.toggleConfirmationDialog();
        this.setState({
            processing: true,
            processingMessage: 'Removing a course...'
        });
        axios({
            url: `${ENROLLMENTS_API_URL}/courses/${this.state.selected.id}`,
            method: 'DELETE'
        })
            .then(() => {
                this.setState(state => {
                    this.selection.setAllSelected(false);
                    return {
                        processing: false,
                        message: 'Course successfully removed',
                        messageType: MessageBarType.success,
                        courses: state.courses.filter(course => course.id !== state.selected.id),
                        selected: null
                    }
                });
                this.resetMessage();
            })
            .catch(error => {
                console.log(JSON.stringify(error));
                this.setState({
                    processing: false,
                    message: 'Unable to remove course',
                    messageType: MessageBarType.error
                });
                this.resetMessage();
            });
    }

    private showDialog = (editing: boolean) => {
        this.setState({showDialog: true, editing: editing});
    }

    private dismissDialog = () => {
        this.setState({showDialog: false});
    }

    private toggleConfirmationDialog = () => {
        this.setState((state) => ({showConfirmationDialog: !state.showConfirmationDialog}));
    }

    private onCreate = (course: Course) => {
        this.setState((state) => {
            let {courses} = state;
            courses.push(course);
            this.props.onCountChange(courses.length);
            return {
                message: 'Student Created Successfully',
                courses: courses.slice()
            };
        });
        this.resetMessage();
    }

    private onEdit = (course: Course) => {
        this.setState((state) => {
            let {courses} = state;
            let index = courses.findIndex(c => c.id === course.id);
            courses[index] = course;
            return {
                courses: courses.slice(),
                selected: course,
                message: 'Student Updated Successfully'
            };
        });
        this.resetMessage();
    }

    private resetMessage = () => {
        this.timeoutID = setTimeout(() => {
            this.setState({
                message: null
            })
        }, 5000);
    }

    columns: IColumn[] = [
        {
            key: 'name',
            name: 'Name',
            ariaLabel: 'Name',
            fieldName: 'name',
            minWidth: 600,
            maxWidth: 600,
            styles: {root: {color: ddColor}}
        },
        {
            key: 'startDate',
            name: 'Start Date',
            ariaLabel: 'Start Date',
            fieldName: 'startDate',
            minWidth: 300,
            maxWidth: 500,
            styles: {root: {color: ddColor}},
            onRender: (course: Course) => {
                return <span>{toDate(course.startDate)}</span>;
            }
        },
        {
            key: 'endDate',
            name: 'End Date',
            ariaLabel: 'End Date',
            fieldName: 'endDate',
            minWidth: 300,
            maxWidth: 500,
            styles: {root: {color: ddColor}},
            onRender: (course: Course) => {
                return <span>{toDate(course.endDate)}</span>;
            }
        }
    ];
}