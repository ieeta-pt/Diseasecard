import React, {useState} from 'react';
import { makeStyles, withStyles } from "@material-ui/core/styles";
import { orange } from '@material-ui/core/colors';
import TreeItem from '@material-ui/lab/TreeItem';
import TreeView from '@material-ui/lab/TreeView';
import PropTypes from 'prop-types';
import {useDispatch, useSelector} from "react-redux";
import {getListOfIds, getSourceURL, getStatus, selectNetwork, showFrame} from "./diseaseSlice";
import {IconButton, Tooltip, Typography, Switch, FormGroup} from "@material-ui/core";
import { ArrowDropDown, ArrowRight, BubbleChart } from "@material-ui/icons";
import FormControlLabel from '@material-ui/core/FormControlLabel';
import {Col, Row} from "react-bootstrap";

export const DiseaseContentTree = () => {
    const [isCheck, setCheck] = useState(false)
    const network = useSelector(selectNetwork)
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

    const useStyles = makeStyles({
        root: {
            height: 264,
            flexGrow: 1,
            maxWidth: 400,
        },
    });

    const classes = useStyles();

    const renderTree = (nodes) => (
        <StyledTreeItem key={nodes.id} nodeId={nodes.id} labelText={nodes.name} hasChildren={ Array.isArray(nodes.children) } >
            {Array.isArray(nodes.children) ? nodes.children.map((node) => renderTree(node)) : null}
        </StyledTreeItem>
    );

    const handleSelect = (event, nodeIds) => {
        if (nodeIds.includes(":")) {
            dispatch(getSourceURL(nodeIds))
            dispatch(showFrame(true))
        }
    };

    const handleToggle = (event, nodeIds) => {
        setExpanded(nodeIds);
    };

    const OrangeSwitch = withStyles({
        switchBase: {
            color: orange[400],
            '&$checked': {
                color: orange[600],
            },
            '&$checked + $track': {
                backgroundColor: orange[500],
            },
        },
        checked: {},
        track: {},
    })(Switch);

    const expandTree = (event) => {
        setCheck(event.target.checked);
        if (!isCheck)   setExpanded( listOfIds )
        else            setExpanded(["root"] )
    };

    if (status === 'succeeded'){
        tree = <div>
                    <div style={{width: "100%", display: "table"}}>
                        <div style={{display: "table-row"}}>
                            <FormGroup style={{paddingLeft: "8%", alignItems: "center", display:"table-cell"}}>
                                <FormControlLabel
                                    style={{paddingTop: "8%"}}
                                    control={<OrangeSwitch checked={ isCheck } onChange={ expandTree } name="checkedA" size="small" />}
                                    label="Expand"
                                />
                            </FormGroup>

                            <Tooltip title="Graph" placement="top" style={{display:"table-cell"}}>
                                <IconButton aria-label="delete" className={classes.margin} onClick={() => { dispatch(showFrame(false))}}>
                                    <BubbleChart fontSize="small" color={orange[400]}/>
                                </IconButton>
                            </Tooltip>
                        </div>

                    </div>



                    <TreeView
                        style={{paddingTop: "3%"}}
                        className={classes.root}
                        defaultCollapseIcon={<ArrowDropDown /> }
                        defaultExpandIcon={<ArrowRight />}
                        expanded={expanded}
                        onNodeToggle={handleToggle}
                        onNodeSelect={handleSelect}
                    >
                        {renderTree( { "id":"root", "name": "Sources", "children" : network} )}
                    </TreeView>
               </div>


    }


    return (
        <div style={{ paddingTop: "5%", paddingLeft: "4%", overflow: "scroll" , height: "90vh"}}>
            { tree }
        </div>
    );

}