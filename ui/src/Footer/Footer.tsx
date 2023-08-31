import React from "react";
import {Text} from "@fluentui/react";
import {ddColor} from "../commons/styles.util";


export class Footer extends React.Component {

    render() {
        return (
            <div style={{ textAlign:"center", marginTop: 20}}>
                <Text variant={'mediumPlus'} style={{color: ddColor}}>
                    © {new Date().getFullYear()} Datadog University
                </Text>
            </div>
        );
    }
}