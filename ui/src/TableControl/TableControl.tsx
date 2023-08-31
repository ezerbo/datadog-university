import React, { Component } from "react";
import {IconButton, ITooltipHostStyles, mergeStyleSets, TooltipHost} from "@fluentui/react";
import {dangerColor} from "../commons/styles.util";

const classNames = mergeStyleSets({
    buttonWithRightMargin: {
        marginRight: '5px'
    },
    deleteButton: {
        color: dangerColor
    }
});

const hostStyles: Partial<ITooltipHostStyles> = { root: { display: 'inline-block' } };
const calloutProps = { gapSpace: 0 };

export interface TableFooterProps {
    selection?: any;
    hideCrudButtons?: boolean;
    disabledUntilSelection: boolean;
    disableAdd?: boolean;
    disableEdit?: boolean;
    onAddClick: () => void;
    onEditClick: () => void;
    onDeleteClick: () => void;
}

export class TableControl extends Component<TableFooterProps, {}> {

    public render() {
        let { hideCrudButtons, disableAdd, disableEdit } = this.props;
        return (
            <div>
                {
                    !hideCrudButtons && (
                        <div style={{ float: 'left' }}>
                            {
                                !disableAdd && (
                                    <TooltipHost
                                        content="Add"
                                        id={"add_btn"}
                                        calloutProps={calloutProps}
                                        styles={hostStyles}
                                    >
                                        <IconButton
                                            iconProps={{ iconName: 'Add' }}
                                            className={classNames.buttonWithRightMargin}
                                            onClick={this.props.onAddClick}
                                        />
                                    </TooltipHost>
                                )
                            }
                            {
                                !disableEdit && (
                                    <TooltipHost
                                        content="Edit"
                                        id={"edit_btn"}
                                        calloutProps={calloutProps}
                                        styles={hostStyles}
                                    >
                                        <IconButton
                                            iconProps={{ iconName: 'Edit' }}
                                            className={classNames.buttonWithRightMargin}
                                            disabled={this.props.disabledUntilSelection}
                                            onClick={this.props.onEditClick}
                                        />
                                    </TooltipHost>
                                )
                            }
                            <TooltipHost
                                content="Delete"
                                id={"delete_btn"}
                                calloutProps={calloutProps}
                                styles={hostStyles}
                            >
                                <IconButton
                                    iconProps={{ iconName: 'Delete' }}
                                    className={classNames.deleteButton}
                                    disabled={this.props.disabledUntilSelection}
                                    onClick={this.props.onDeleteClick}
                                />
                            </TooltipHost>
                        </div>
                    )
                }
                <div style={{ clear: 'left' }}></div>
            </div>
        );
    }
}