# Lookbook API: Git Workflow & Branching Strategy

This document outlines the Git workflow and branching strategy for the Lookbook API project.

## Branch Structure

We follow a modified Gitflow workflow with the following branches:

### Primary Branches

- **`main`**: Production-ready code that has been released or is ready to be released
- **`develop`**: Integration branch for features in development; contains the latest delivered development changes

### Supporting Branches

- **Feature branches**: For new features and non-emergency bug fixes
- **Release branches**: For finalizing a release
- **Hotfix branches**: For critical bug fixes that must be applied to production immediately
- **Experimental branches**: For trying out ideas without affecting the main development

## Branch Naming Conventions

All branch names should be lowercase and use hyphens as separators, following this format:

- Feature branches: `feature/short-description`
- Bug fix branches: `bugfix/short-description` or `fix/short-description`
- Release branches: `release/version-number`
- Hotfix branches: `hotfix/short-description`
- Experimental branches: `experimental/short-description`

Examples:
- `feature/user-authentication`
- `feature/wardrobe-management`
- `bugfix/login-error`
- `release/1.0.0`
- `hotfix/security-vulnerability`
- `experimental/new-ui-pattern`

## Workflow Process

### Feature Development

1. **Create a feature branch**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/your-feature-name
   ```

2. **Develop your feature**
   - Make commits with meaningful messages following our [commit message conventions](#commit-message-conventions)
   - Keep your branch updated with `develop` by regularly merging or rebasing:
     ```bash
     git fetch origin
     git merge origin/develop
     # or
     git rebase origin/develop
     ```

3. **Submit a Pull Request**
   - Push your feature branch to the remote repository:
     ```bash
     git push origin feature/your-feature-name
     ```
   - Create a pull request to merge your branch into `develop`
   - Fill out the pull request template with details about your changes

4. **Code Review and Merge**
   - Ensure at least one team member reviews your code
   - Address any feedback or requested changes
   - Once approved, the feature will be merged into `develop`
   - Delete your branch after it's been merged

### Bug Fixes

Follow the same process as feature development, but prefix your branch with `bugfix/` or `fix/`.

### Releases

1. **Create a release branch**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b release/1.0.0
   ```

2. **Finalize the release**
   - Make final adjustments, version updates, and documentation
   - Only bug fixes should be committed to release branches, not new features

3. **Merge to main and develop**
   - Once the release is ready, create a PR to merge into `main`
   - After merging to `main`, tag the release with its version number:
     ```bash
     git tag -a v1.0.0 -m "Version 1.0.0"
     git push origin v1.0.0
     ```
   - Then merge back into `develop` to incorporate any changes made during release finalization

### Hotfixes

1. **Create a hotfix branch from main**
   ```bash
   git checkout main
   git pull origin main
   git checkout -b hotfix/critical-bug
   ```

2. **Fix the issue**
   - Make the necessary changes to address the critical issue

3. **Merge to main and develop**
   - Create a PR to merge into `main`
   - After merging to `main`, tag with an updated version number
   - Then merge the hotfix into `develop` as well

## Commit Message Conventions

We follow a standardized commit message format to maintain a clean and meaningful git history:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types
- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation changes
- **style**: Changes that don't affect code functionality (formatting, missing semi-colons, etc.)
- **refactor**: Code changes that neither fix a bug nor add a feature
- **test**: Adding or modifying tests
- **chore**: Changes to build process, auxiliary tools, libraries, etc.

### Scope
The scope provides context about what part of the codebase is affected. Examples:
- user
- auth
- wardrobe
- outfit
- etc.

### Subject
- Use imperative mood ("Add" not "Added" or "Adds")
- Don't capitalize the first letter
- No period at the end

### Examples

```
feat(auth): add JWT token refresh functionality

fix(user): resolve email validation issue

docs(readme): update installation instructions

refactor(wardrobe): simplify item addition process
```

## Pull Request Process

1. **Create a pull request from your branch to the appropriate target branch**
   - Feature branches → `develop`
   - Hotfix branches → `main` (and subsequently to `develop`)
   - Release branches → `main` (and subsequently to `develop`)

2. **Fill out the PR template with:**
   - Description of changes
   - Issue number(s) addressed
   - Type of change (feature, bugfix, etc.)
   - Testing performed
   - Checklist of completed items

3. **Request reviewers**
   - At least one review is required before merging

4. **Address feedback**
   - Make requested changes
   - Push additional commits to the same branch
   - Respond to comments

5. **Merge strategy**
   - We use "Squash and merge" for feature branches to maintain a clean history
   - We use "Merge commit" for release and hotfix branches to preserve the commit history

6. **Clean up**
   - Delete branches after they are merged

## CI/CD Integration

Our branching strategy integrates with our CI/CD pipelines as follows:

- **Push to any branch**: Runs tests and code quality checks
- **PRs to `develop`**: Runs tests, linting, and builds the application
- **Merge to `develop`**: Automatically deploys to the development environment
- **Merge to `main`**: Triggers deployment to staging, awaiting manual approval for production

## Special Considerations

### Long-running Feature Branches

For features that take longer than 2 weeks to complete:

1. Consider breaking them into smaller, more manageable chunks
2. Regularly merge changes from `develop` to prevent drift
3. Create a feature flag to hide the incomplete feature in production if needed

### Experimental Work

For experimental features that may or may not be included:

1. Create branches with the `experimental/` prefix
2. Do not expect these branches to be merged directly
3. If the experiment proves successful, create a proper feature branch and implement it properly

## Git Configuration and Best Practices

### Recommended Git Configuration

```bash
# Set your identity
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Set default behavior for line endings
git config --global core.autocrlf input  # For Mac/Linux
git config --global core.autocrlf true   # For Windows

# Enable helpful rebase options
git config --global pull.rebase true
git config --global rebase.autoStash true
```

### Best Practices

1. **Commit often, push regularly**
   - Make small, focused commits
   - Push your branch regularly to avoid losing work

2. **Write meaningful commit messages**
   - Follow the commit message conventions
   - Explain "why" more than "what" (the code shows what changed)

3. **Keep branches updated**
   - Regularly incorporate changes from the target branch
   - Resolve conflicts promptly

4. **Don't commit sensitive information**
   - No API keys, passwords, or secrets in the codebase
   - Use environment variables or a secrets management system

5. **Don't commit generated files**
   - Keep the repository clean of build artifacts and dependencies
   - Update the `.gitignore` file as needed

## Troubleshooting Common Issues

### Resolving Merge Conflicts

```bash
# When you encounter merge conflicts
git status  # Identify conflicted files
# Edit files to resolve conflicts
git add <resolved-files>
git commit  # or continue rebase with 'git rebase --continue'
```

### Undoing Local Changes

```bash
# Discard all local changes in your working directory
git reset --hard

# Discard local changes to a specific file
git checkout -- <file>

# Undo the most recent commit (keeping changes staged)
git reset --soft HEAD~1

# Undo the most recent commit (discarding changes)
git reset --hard HEAD~1
```

### Recovering Lost Changes

```bash
# View reflog to find lost commits
git reflog

# Recover a discarded branch or commit
git checkout -b recovered-branch <commit-hash>
``` 