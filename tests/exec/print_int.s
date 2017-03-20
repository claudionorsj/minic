	.text
	.globl main
print_int:
	subq $16, %rsp
	movq %rbx, 0(%rsp)
	movq %rdi, 8(%rsp)
	movq 8(%rsp), %rax
	movq $10, %rdi
	movq %rdx, %rbx
	movq $0, %rdx
	idivq %rdi
	movq %rax, %rbx
	movq 8(%rsp), %rdi
	cmpq $9, %rdi
	jg L11
L9:
	movq 8(%rsp), %rdi
	movq $10, %rcx
	imulq %rbx, %rcx
	subq %rcx, %rdi
	addq $48, %rdi
	call putchar
	movq $0, %rax
	movq 0(%rsp), %rbx
	addq $16, %rsp
	ret
L11:
	movq %rbx, %rdi
	call print_int
	jmp L9
main:
	movq $42, %rdi
	call print_int
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
