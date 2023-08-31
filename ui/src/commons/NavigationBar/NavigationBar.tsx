import {CommandBar, ICommandBarItemProps} from "@fluentui/react";
import React from "react";
import {useNavigate} from "react-router-dom";

export const NavigationBar = () => {

    let navigate = useNavigate();

    const items: ICommandBarItemProps[] = [
        {
            key: 'home',
            text: 'Home',
            iconProps: { iconName: 'Home' },
            onClick: () => navigate('/')
        },
        {
            key: 'students',
            text: 'Students',
            cacheKey: 'students', // changing this key will invalidate this item's cache
            iconProps: { iconName: 'People' },
            onClick: () => navigate('/students')
        },
        {
            key: 'courses',
            text: 'Courses',
            iconProps: { iconName: 'FolderList' },
            onClick: () => navigate('/courses')
        },
        {
            key: 'enrollments',
            text: 'Enrollments',
            iconProps: { iconName: 'FabricUserFolder' },
            onClick: () => navigate('/enrollments')
        },
        {
            key: 'grades',
            text: 'Grades',
            iconProps: { iconName: 'BulletedList' },
            onClick: () => navigate('/grades')
        },
        {
            key: 'tuition',
            text: 'Tuition',
            iconProps: { iconName: 'Money' },
            onClick: () => navigate('/tuition')
        }
    ];

    return (
        <div>
            <CommandBar
                items={items}
                ariaLabel="Menu Actions"
            />
        </div>
    );
}