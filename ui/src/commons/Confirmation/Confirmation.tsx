import React, {CSSProperties} from 'react';
import {
    PrimaryButton,
    Dialog,
    DialogFooter,
    DialogType,
    Text,
    Separator
} from '@fluentui/react';

export interface ConfirmationProps {
    question: string;
    confirmationBtnTxt: string;
    onConfirmation: () => void;
    onDismiss: () => void;
    width: number;
    confirmationIconName: string;
    confirmationBtnStyle: CSSProperties;
}

export class Confirmation extends React.Component<ConfirmationProps, {}> {

    public render(): React.ReactElement<ConfirmationProps> {
        const {width, confirmationBtnTxt, confirmationIconName, confirmationBtnStyle} = this.props;
        return (
            <Dialog
                hidden={false}
                onDismiss={this.props.onDismiss}
                dialogContentProps={{
                    type: DialogType.close,
                    title: 'Confirmation'
                }}
                modalProps={{
                    titleAriaId: 'confirmationTitle',
                    subtitleAriaId: 'confirmationSubtitle',
                    isBlocking: true,
                    containerClassName: 'ms-dialogMainOverride'
                }}
                minWidth={width}
            >
                <div>
                    <Text variant={'mediumPlus'}>
                        {this.props.question}
                    </Text>
                </div>
                <Separator />
                <DialogFooter>
                    <PrimaryButton
                        onClick={this.props.onConfirmation}
                        text={confirmationBtnTxt}
                        iconProps={{ iconName: confirmationIconName }}
                        style={confirmationBtnStyle}
                    />
                    <PrimaryButton
                        onClick={this.props.onDismiss}
                        text="Cancel"
                        iconProps={{ iconName: 'Cancel' }} />
                </DialogFooter>
            </Dialog>
        );
    }

}