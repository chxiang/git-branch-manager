package com.geostar.geostack.git_branch_manager.web;

import com.geostar.geostack.git_branch_manager.pojo.GitProject;
import com.geostar.geostack.git_branch_manager.service.IGitRepositoryService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * 首页对应的模板名称
     */
    private static final String INDEX_HTML = "list";
    @Resource
    private IGitRepositoryService gitRepositoryService;

    /**
     * 首页展示
     *
     * @return
     */
    @RequestMapping("/")
    public String index(Model model) {
        list(model);
        return INDEX_HTML;
    }

    /**
     * 列表展示
     *
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return "list";
    }

    /**
     * 克隆或者拉取最新代码
     *
     * @param model
     * @return
     */
    @RequestMapping({"cloneOrPull"})
    public String cloneOrPull(Model model) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.cloneOrPull(gitProject);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return INDEX_HTML;
    }

    /**
     * 创建分支
     *
     * @param model
     * @param branchName
     * @return
     */
    @RequestMapping({"/createBranch/{branchName}"})
    public String createBranch(Model model, @PathVariable("branchName") String branchName) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.createBranch(gitProject, branchName);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
            gitProject.getBranchList().add(branchName);
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return INDEX_HTML;
    }

    /**
     * 切换分支
     *
     * @param model
     * @param branchName
     * @return
     */
    @RequestMapping({"/switchBranch/{branchName}"})
    public String switchBranchAll(Model model, @PathVariable("branchName") String branchName) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.switchBranch(gitProject, branchName);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return INDEX_HTML;
    }

    /**
     * 推送代码
     *
     * @param model
     * @return
     */
    @RequestMapping({"/push/{message}"})
    public String push(Model model, @PathVariable(value = "message") String inputMessage) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.push(gitProject, inputMessage);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return INDEX_HTML;
    }

    /**
     * 删除项目的当前分支，不允许删除master和develop
     *
     * @param model
     * @return
     */
    @RequestMapping({"/deleteBranch"})
    public String deleteBranch(Model model) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.deleteBranch(gitProject);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return INDEX_HTML;
    }

    /**
     * 创建标签
     *
     * @param model
     * @param tagName
     * @param tagLog
     * @return
     */
    @RequestMapping({"/createTag/{tagName}/{tagLog}"})
    public String createTag(Model model, @PathVariable(value = "tagName") String tagName, @PathVariable(value = "tagLog") String tagLog) {
        List<GitProject> projects = gitRepositoryService.getAllGitProject();
        for (GitProject gitProject : projects) {
            try {
                gitRepositoryService.createTag(gitProject, tagName, tagLog);
                gitRepositoryService.updateGitProjectInfo(gitProject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        List<String> branchIntersect = gitRepositoryService.getBranchIntersect(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("branchIntersect", branchIntersect);
        return INDEX_HTML;
    }

}
