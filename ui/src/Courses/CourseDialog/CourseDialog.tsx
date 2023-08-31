import {Component} from "react";
import * as Yup from "yup";
import {Formik, Form, Field} from 'formik';
import {
    DefaultButton,
    DialogFooter,
    DialogType,
    Dialog,
    IStackProps,
    PrimaryButton,
    TextField,
    Stack,
    MessageBarType,
    MessageBar, Spinner, Separator
} from "@fluentui/react";
import axios from "axios";
import {formatErrors, handleHttpErrors} from "../../commons/http.util";
import {CustomDatePicker} from "../../commons/Custom/CustomDatePicker";
import {ENROLLMENTS_API_URL} from "../../index";
import {Course} from "../Course";

const Schema = Yup.object().shape({
    name: Yup.string()
        .min(3, 'Should be at least 3 characters long')
        .max(50, 'Should be at most 50 characters')
        .required('Please enter the name'),
    startDate: Yup.date()
        .required('The start date is required'),
    endDate: Yup.date()
        .required('The end date is required')
});

interface DialogProps {
    show: boolean;
    course: Course;
    onCreateSuccess: (course: Course) => void;
    onEditSuccess: (course: Course) => void;
    onDismiss: () => void;
}

interface DialogState {
    processing: boolean
    errorMessage?: JSX.Element
}

export class CourseDialog extends Component<DialogProps, DialogState> {

    constructor(props: DialogProps) {
        super(props);
        this.state = {processing: false};
    }

    componentDidUpdate(prevProps: Readonly<DialogProps>, prevState: Readonly<DialogState>, snapshot?: any) {
        if (this.props.course !== prevProps.course) {
            this.setState({errorMessage: null});
        }
    }

    public render() {
        const columnProps: Partial<IStackProps> = {
            tokens: {childrenGap: 15},
            styles: {root: {width: 550}}
        };
        const {errorMessage, processing} = this.state;
        const {course} = this.props;
        const title = course ? 'Edit a Course' : 'Create a Course';
        const subText = course ? 'Edit a course\'s details' : 'Add a new course';
        return (
            <Dialog
                hidden={!this.props.show}
                onDismiss={this.dismissDialog}
                dialogContentProps={{
                    type: DialogType.close,
                    title: title,
                    closeButtonAriaLabel: 'Close',
                    subText: subText,
                }}
                modalProps={{
                    titleAriaId: 'myLabelId',
                    subtitleAriaId: 'mySubTextId',
                    isBlocking: true,
                    containerClassName: 'ms-dialogMainOverride',
                }}
                minWidth={600}
            >
                {
                    errorMessage && (
                        <div>
                            {errorMessage}
                        </div>
                    )
                }

                {
                    processing && (
                        <div>
                            <Spinner label="Saving changes..."/>
                        </div>
                    )
                }

                <Formik
                    initialValues={course ?
                        {
                            id: course.id,
                            name: course.name,
                            startDate: new Date(course.startDate),
                            endDate: new Date(course.endDate)
                        } :
                        {
                            id: undefined,
                            name: '',
                            startDate: new Date(),
                            endDate: new Date(),
                        }
                    }
                    validationSchema={Schema}
                    onSubmit={values => this.onSubmit(values as Course)}
                >
                    {({errors, touched, handleChange, handleBlur, values}) => (
                        <Form>
                            <Stack horizontal tokens={{childrenGap: 50}} styles={{root: {width: 550}}}>
                                <Stack {...columnProps}>
                                    <input type="hidden" value={values.id}/>
                                    <TextField
                                        name="name"
                                        value={values.name}
                                        label="Name"
                                        onChange={handleChange}
                                        onBlur={handleBlur}
                                        iconProps={{iconName: 'Education'}}
                                        errorMessage={errors.name && touched.name ? errors.name : undefined}
                                        required
                                    />
                                    <Field
                                        name="startDate"
                                        label="Start Date"
                                        placeholder="Select the start date"
                                        component={CustomDatePicker}
                                    />
                                    <Field
                                        name="endDate"
                                        label="End Date"
                                        placeholder="Select the end date"
                                        component={CustomDatePicker}
                                    />
                                </Stack>
                            </Stack>
                            <Separator/>
                            <DialogFooter>
                                <PrimaryButton
                                    type="submit"
                                    text="Save"
                                    iconProps={{iconName: 'Add'}}
                                    styles={{rootHovered: {backgroundColor: '#0e4066'}, root: {marginRight: '8px'}}}/>
                                <DefaultButton
                                    onClick={this.dismissDialog}
                                    text="Cancel"
                                    iconProps={{iconName: 'Cancel'}}/>
                            </DialogFooter>
                        </Form>
                    )}
                </Formik>
            </Dialog>
        );
    }

    private dismissDialog = () => {
        this.dismissMessage();
        this.props.onDismiss();
    }

    private onSubmit = (values: Course) => {
        if (values.id) {
            this.update(values);
        } else {
            this.create(values);
        }
    }

    private create = (values: Course) => {
        this.setProcessingStatus(true);
        axios({
            url: `${ENROLLMENTS_API_URL}/courses`,
            method: 'POST',
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify(values)
        })
            .then(handleHttpErrors)
            .then(res => res.data)
            .then(student => {
                this.setProcessingStatus(false);
                this.props.onDismiss();
                this.props.onCreateSuccess(student);
            })
            .catch(error => {
                console.log(JSON.stringify(error));
                this.setState({
                    errorMessage: this.getMessage(formatErrors(error)),
                    processing: false
                });
            });
    }

    private update = (student: Course) => {
        this.setProcessingStatus(true);
        axios({
            url: `${ENROLLMENTS_API_URL}/courses/${student.id}`,
            method: 'PUT',
            headers: {"Content-Type": "application/json"},
            data: JSON.stringify(student)
        })
            .then(handleHttpErrors)
            .then(res => res.data)
            .then(student => {
                this.setProcessingStatus(false);
                this.props.onDismiss();
                this.props.onEditSuccess(student);
            })
            .catch(error => {
                console.log(JSON.stringify(error));
                this.setState({
                    errorMessage: this.getMessage(formatErrors(error)),
                    processing: false
                });
            });
    }

    private setProcessingStatus = (processing: boolean) => {
        this.setState({processing: processing});
    }

    private getMessage = (message: string) => {
        return (
            <MessageBar
                messageBarType={MessageBarType.error}
                isMultiline={false}
                onDismiss={this.dismissMessage}>
                {message}
            </MessageBar>
        );
    }

    private dismissMessage = () => {
        this.setState({errorMessage: undefined});
    }
}