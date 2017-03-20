	.text
	.globl main
fact:
	subq $8, %rsp
	cmpq $1, %rdi
	jle L7
	movq %rdi, 0(%rsp)
	subq $1, %rdi
	call fact
	imulq %rax, 0(%rsp)
L1:
	movq 0(%rsp), %rax
	addq $8, %rsp
	ret
L7:
	movq $1, 0(%rsp)
	jmp L1
main:
	subq $8, %rsp
	movq $0, %rdi
	movq %rdi, 0(%rsp)
L23:
	movq 0(%rsp), %rdi
	cmpq $4, %rdi
	jle L21
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
L21:
	movq 0(%rsp), %rdi
	call fact
	movq %rax, %rdi
	addq $65, %rdi
	call putchar
	movq 0(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 0(%rsp)
	jmp L23
	.data
