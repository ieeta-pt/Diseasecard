import React, {useState} from 'react';
import { useDispatch, useSelector } from "react-redux";
import   PropTypes  from 'prop-types';
import {getListOfIds, getReady, getSourceURL, getStatus, selectNetwork, selectTree, showFrame} from "./diseaseSlice";
import { Typography, Divider, Paper, Tooltip } from "@material-ui/core";
import { makeStyles, withStyles } from '@material-ui/core/styles';
import { ArrowDropDown, ArrowRight, BubbleChart, AllOut, FormatListBulleted } from "@material-ui/icons";
import { TreeItem, TreeView, ToggleButtonGroup, ToggleButton }  from '@material-ui/lab';


export const DiseaseContentTree = () => {
    const [view, setView] = useState('graph');
    const [expand, setExpand] = useState('');
    const [isCheck, setCheck] = useState(false)
    const info = useSelector(selectTree)
    const listOfIds = useSelector(getListOfIds)
    const [expanded, setExpanded] = useState(["root"]);
    const status = useSelector(getStatus)
    const dispatch = useDispatch();
    let tree;


    const useTreeItemStyles = makeStyles((theme) => ({
        root: {
            color: theme.palette.text.secondary,
            '&:hover > $content': {
                backgroundColor: theme.palette.action.hover,
            },
            '&:focus > $content, &$selected > $content': {
                backgroundColor: `var(--tree-view-bg-color, ${theme.palette.grey[400]})`,
                color: 'var(--tree-view-color)',
            },
            '&:focus > $content $label, &:hover > $content $label, &$selected > $content $label': {
                backgroundColor: 'transparent',
            },
        },
        content: {
            color: theme.palette.text.secondary,
            borderTopRightRadius: theme.spacing(2),
            borderBottomRightRadius: theme.spacing(2),
            paddingRight: theme.spacing(1),
            fontWeight: theme.typography.fontWeightMedium,
            '$expanded > &': {
                fontWeight: theme.typography.fontWeightRegular,
            },
        },
        group: {
            marginLeft: 0,
            '& $content': {
                paddingLeft: theme.spacing(2),
            },
        },
        expanded: {},
        selected: {},
        label: {
            fontWeight: 'inherit',
            color: 'inherit',
        },
        labelRoot: {
            display: 'flex',
            alignItems: 'center',
            padding: theme.spacing(0.5, 0),
        },
        labelIcon: {
            marginRight: theme.spacing(1),
        },
        labelText: {
            fontWeight: 'inherit',
            flexGrow: 1,
        },
    }));

    function StyledTreeItem(props) {
        const classes = useTreeItemStyles();
        const { labelText, hasChildren, labelInfo, color, bgColor, ...other } = props;
        return (
            <TreeItem
                label={
                    <div className={classes.labelRoot}>
                        <Typography variant="body2" className={classes.labelText}>
                            { hasChildren
                                ? <span style={{ fontSize: "0.8rem", color: "#212529" }}><b> {labelText} </b></span>
                                : <span style={{ fontSize: "0.7rem", color: "#212529" }}> {labelText} </span>
                            }
                        </Typography>
                        <Typography variant="caption" color="inherit">
                            {labelInfo}
                        </Typography>
                    </div>
                }
                style={{
                    '--tree-view-color': color,
                    '--tree-view-bg-color': bgColor,
                }}
                classes={{
                    root: classes.root,
                    content: classes.content,
                    expanded: classes.expanded,
                    selected: classes.selected,
                    group: classes.group,
                    label: classes.label,
                }}
                {...other}
            />
        );
    }

    StyledTreeItem.propTypes = {
        bgColor: PropTypes.string,
        color: PropTypes.string,
        labelIcon: PropTypes.elementType,
        labelInfo: PropTypes.string,
        labelText: PropTypes.string.isRequired,
        hasChildren: PropTypes.bool.isRequired
    };

    const renderTree = (nodes) => {
        return <StyledTreeItem key={nodes.id} nodeId={nodes.id} labelText={nodes.fullName}
                        hasChildren={Array.isArray(nodes.children)}>
            {Array.isArray(nodes.children) ? nodes.children.map((node) => renderTree(node)) : null}
        </StyledTreeItem>
    };

    const useStyles = makeStyles((theme) => ({
        root: {
            height: 264,
            flexGrow: 1,
            maxWidth: 400,
        },
        paper: {
            display: 'flex',
            border: `1px solid ${theme.palette.divider}`,
            flexWrap: 'wrap',
        },
        divider: {
            margin: theme.spacing(1, 0.5),
        },
    }));

    const classes = useStyles();

    const handleSelect = (event, nodeIds) => {
        if (nodeIds.includes(":")) {
            dispatch(getSourceURL(nodeIds))
            dispatch(showFrame(true))
            setView('none')
        }
    };

    const handleToggle = (event, nodeIds) => {
        setExpanded(nodeIds);
    };

    const StyledToggleButtonGroup = withStyles((theme) => ({
        grouped: {
            margin: theme.spacing(0.5),
            border: 'none',
            '&:not(:first-child)': {
                borderRadius: theme.shape.borderRadius,
            },
            '&:first-child': {
                borderRadius: theme.shape.borderRadius,
            },
        },
    }))(ToggleButtonGroup);

    const handleExpansion = (event, info) => {
        //console.log(info)
        setExpand(info)
        setCheck(!isCheck)
        if (!isCheck)   setExpanded( listOfIds )
        else            setExpanded(["root"] )
    };

    const handleView = (event, info) => {
        setView(info)
        //console.log(info)
        dispatch(showFrame(false))
    };


    if (status === 'succeeded'){
        tree = <div>
                    <Paper elevation={0} className={classes.paper}>
                        <StyledToggleButtonGroup
                            size="small"
                            aria-label="text alignment"
                            onChange={handleExpansion}
                            value={expand}
                        >

                            <ToggleButton value="check" aria-label="check" >
                                <Tooltip title="Expand Content Tree" aria-label="add" placement="top">
                                    <AllOut />
                                </Tooltip>
                            </ToggleButton>

                        </StyledToggleButtonGroup>
                        <Divider flexItem orientation="vertical" className={classes.divider} />
                        <StyledToggleButtonGroup
                            size="small"
                            exclusive
                            aria-label="text formatting"
                            onChange={handleView}
                            value={view}
                        >

                            <ToggleButton value="graph" aria-label="bold">
                                <Tooltip title="Show Graph" aria-label="add" placement="top">
                                    <BubbleChart />
                                </Tooltip>
                            </ToggleButton>


                            <ToggleButton value="detailed" aria-label="left aligned" disabled>
                                <Tooltip title="Show Detailed Info" aria-label="add" placement="top">
                                    <FormatListBulleted />
                                </Tooltip>
                            </ToggleButton>

                        </StyledToggleButtonGroup>
                    </Paper>

                    <TreeView
                        style={{paddingTop: "4%"}}
                        className={classes.root}
                        defaultCollapseIcon={<ArrowDropDown /> }
                        defaultExpandIcon={<ArrowRight />}
                        expanded={expanded}
                        onNodeToggle={handleToggle}
                        onNodeSelect={handleSelect}
                    >
                        {renderTree( { "id":"root", "name": "Sources", "children" : info} )}
                    </TreeView>
               </div>
    }

    return (
        <div style={{ paddingTop: "5%", paddingLeft: "4%", overflow: "auto" , minHeight:"calc(100vh - 3.5em)", borderRight: '1px solid rgba(0, 0, 0, 0.12)'}}>
            { tree }
        </div>
    );

}