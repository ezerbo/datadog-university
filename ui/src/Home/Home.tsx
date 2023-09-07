import React from "react";
import {Footer} from "../Footer/Footer";
import {Header} from "../Header/Header";
import {NavigationBar} from "../commons/NavigationBar/NavigationBar";
import {
    Icon,
    Image, mergeStyles,
    Separator, StackItem, Text
} from "@fluentui/react";
import {ddColor} from "../commons/styles.util";
import {Stack} from '@fluentui/react/lib/Stack';
import flame_graph from '../imgs/flame-graph.png';


const iconClass = mergeStyles({
    fontSize: 150,
    height: 150,
    width: 150,
    margin: 20,
    color: ddColor
});

export const Home: React.FunctionComponent = () => {

    return (
        <div>
            <Header/>
            <NavigationBar/>
            <div style={{textAlign: "center"}}>
                <Text variant={'xLarge'} style={{color: ddColor}}>
                    Learn Monitoring, Distributed Tracing, Log Management, Synthetics & More
                </Text>
            </div>
            <Separator/>
            <div style={{textAlign: "center"}}>
                <Icon iconName={'BarChart4'} className={iconClass}/>
                <Icon iconName={'PieSingle'} className={iconClass}/>
                <Icon iconName={'TVMonitorSelected'} className={iconClass}/>
                <Icon iconName={'Financial'} className={iconClass}/>
            </div>
            <Stack horizontal style={{justifyContent: "center"}}>
                <StackItem>
                    <Image
                        width={800}
                        height={190}
                        src={flame_graph}
                    />
                </StackItem>
            </Stack>
            <Footer/>
        </div>
    );
}