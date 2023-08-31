import * as React from 'react';
import {Icon, initializeIcons, mergeStyles, Separator, Text} from '@fluentui/react';
import {ddColor} from "../commons/styles.util";

const iconClass = mergeStyles({
    fontSize: 150,
    height: 150,
    width: 150,
    color: ddColor
});

initializeIcons();

export const Header = () => {

    return (
        <div style={{margin: 10, textAlign: "center"}}>
            <Icon iconName={'Education'} className={iconClass}/>
            <div>
                <Text style={{color: ddColor}}>
                    DATADOG UNIVERSITY
                </Text>
            </div>
            <Separator vertical/>
        </div>
    );
}
